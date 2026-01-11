package com.example.be_kiotviet.controller;

import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.entity.Role;
import com.example.be_kiotviet.entity.Shops;
import com.example.be_kiotviet.entity.User;
import com.example.be_kiotviet.repository.RoleRepository;
import com.example.be_kiotviet.repository.ShopRepository;
import com.example.be_kiotviet.repository.UserRepository;
import com.example.be_kiotviet.service.EmailService;
import com.example.be_kiotviet.service.Shop.ShopService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/public/shops")
    @ResponseStatus(HttpStatus.CREATED)
    public ShopResponse create(@Valid @RequestBody ShopRequest request) {
        return service.create(request);
    }

    @GetMapping("/admin/shops/{id}")
    public ShopResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }
    @GetMapping("/admin/shops/{code}")
    public ShopResponse getByCode(@PathVariable String code) {
        return service.getByCode(code);
    }

    @GetMapping("/admin/shops")
    public Page<ShopResponse> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PutMapping("/admin/shops/{id}")
    public ShopResponse update(@PathVariable Long id, @Valid @RequestBody ShopRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/admin/shops/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/public/verify")
    public String verifyEmail(@RequestParam String token, @RequestParam Long shopId) {
        Shops shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Gian hàng không tồn tại"));

        if (!shop.getVerificationToken().equals(token)) {
            return "redirect:/verify-failed.html";
        }

        if (shop.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            return "redirect:/verify-expired.html";
        }

        // Kích hoạt shop
        shop.setIsVerified(true);
        shop.setStatus("ACTIVE");
        shop.setVerificationToken(null);
        shop.setVerificationTokenExpiry(null);
        shopRepository.save(shop);

        // Tìm admin user
        Role adminRole = roleRepository.findByName("Admin")
                .orElseThrow(() -> new RuntimeException("Role Admin không tồn tại"));

        Long adminRoleId = adminRole.getId();
        User adminUser = userRepository.findByShopIdAndRoleId(shopId, adminRoleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản admin"));

        // Lấy tempPassword từ Redis
        String tempPassword = redisTemplate.opsForValue().get("temp_password:" + shopId);
        if (tempPassword == null) {
            throw new RuntimeException("Mật khẩu tạm thời đã hết hạn hoặc không tồn tại");
        }

        // Gửi email chứa username + password
        emailService.sendCredentialsEmail(shop.getEmail(), adminUser.getUsername(), tempPassword);

        // Xóa ngay plain password khỏi Redis
        redisTemplate.delete("temp_password:" + shopId);

        // Redirect về trang thành công (FE)
        return "redirect:http://localhost:5173/verify-success";
    }
}
