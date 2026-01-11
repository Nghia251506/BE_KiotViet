package com.example.be_kiotviet.service.Shop;

import com.example.be_kiotviet.config.SecurityConfig;
import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.entity.BusinessType;
import com.example.be_kiotviet.entity.Shops;
import com.example.be_kiotviet.entity.User;
import com.example.be_kiotviet.exception.ResourceNotFoundException;
import com.example.be_kiotviet.mapper.ShopMapper;
import com.example.be_kiotviet.repository.RoleRepository;
import com.example.be_kiotviet.repository.ShopRepository;
import com.example.be_kiotviet.repository.UserRepository;
import com.example.be_kiotviet.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository repository;
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final  EmailService  emailService;
    private final RoleRepository roleRepository;
    private final ShopMapper mapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime getTokenExpiry() {
        return LocalDateTime.now().plusHours(24); // token hết hạn sau 24h
    }

    @Override
    @Transactional
    public ShopResponse create(ShopRequest request) {
        // Validate
        if (repository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại!");
        }
        if (repository.existsByCode(request.getCode())) {
            throw new RuntimeException("Mã gian hàng đã tồn tại!");
        }
        if (request.getBusinessType() == null) {
            throw new RuntimeException("Vui lòng chọn mô hình kinh doanh");
        }
        if (request.getBusinessType() == BusinessType.OTHER &&
                (request.getBusinessDescription() == null || request.getBusinessDescription().trim().isEmpty())) {
            throw new RuntimeException("Vui lòng mô tả ngành nghề khi chọn 'Khác'");
        }

        // Tạo shop
        Shops entity = mapper.toEntity(request);
        entity.setStatus("PENDING_VERIFICATION");
        entity.setVerificationToken(UUID.randomUUID().toString());
        entity.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        entity.setIsVerified(false);

        entity = repository.save(entity);

        // Tạo admin user
        User adminUser = new User();
        adminUser.setShop(entity);
        adminUser.setUsername(request.getPhone());
        adminUser.setEmail(request.getEmail());
        adminUser.setPhone(request.getPhone());
        adminUser.setIsActive(true);

        String tempPassword = RandomStringUtils.randomAlphanumeric(10); // password thực tế gửi cho khách
        adminUser.setPassword(securityConfig.passwordEncoder().encode(tempPassword)); // hash để login
        adminUser.setRole(roleRepository.findByName("Admin").orElseThrow());
        adminUser.setMustchangepassword(true);

        Instant now = Instant.now();
        adminUser.setCreatedAt(now);
        adminUser.setUpdatedAt(now);

        userRepository.save(adminUser);

        // Lưu tempPassword vào Redis (expire 24h)
        redisTemplate.opsForValue().set(
                "temp_password:" + entity.getId(),
                tempPassword,
                24, TimeUnit.HOURS
        );

        // Gửi email xác thực (không gửi password lúc này)
        emailService.sendVerificationEmail(
                request.getEmail(),
                entity.getVerificationToken(),
                entity.getId()
        );

        // Response sạch, không lộ password
        ShopResponse response = mapper.toResponse(entity);
        response.setDefaultUsername(request.getPhone());
//        response.setMessage("Tạo gian hàng thành công! Vui lòng kiểm tra email để xác thực và nhận thông tin đăng nhập.");

        return response;
    }

    @Override
    public ShopResponse update(Long id, ShopRequest request) {
        Shops entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop không tồn tại"));
        mapper.updateEntity(entity, request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public ShopResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Shop không tồn tại"));
    }
    @Override
    public ShopResponse getByCode(String code){
        return repository.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(()-> new ResourceNotFoundException("Shop không tồn tại"));
    }

    @Override
    public Page<ShopResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Shop không tồn tại");
        }
        repository.deleteById(id);
    }
}
