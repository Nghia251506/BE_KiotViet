package com.example.be_kiotviet.service.Supplier;

import com.example.be_kiotviet.dto.Supplier.SupplierRequest;
import com.example.be_kiotviet.dto.Supplier.SupplierResponse;
import com.example.be_kiotviet.entity.ShopSupplier;
import com.example.be_kiotviet.entity.Shops;
import com.example.be_kiotviet.entity.Supplier;
import com.example.be_kiotviet.mapper.SupplierMapper;
import com.example.be_kiotviet.repository.ShopRepository;
import com.example.be_kiotviet.repository.ShopSupplierRepository;
import com.example.be_kiotviet.repository.SupplierRepository;
import com.example.be_kiotviet.service.Supplier.SupplierService;
import com.example.be_kiotviet.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ShopSupplierRepository shopSupplierRepository;
    private final ShopRepository shopsRepository;
    private final SupplierMapper mapper;

    private Long getCurrentShopId() {
        return TenantContext.getCurrentShopId();
    }

    @Override
    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        Long shopId = getCurrentShopId();

        // 1. Validate Supplier global
        if (supplierRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Mã nhà cung cấp đã tồn tại trong hệ thống");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email nhà cung cấp đã tồn tại");
        }

        // 2. Tạo Supplier global
        Supplier supplier = mapper.toEntity(request);
        if (supplier.getStatus() == null || supplier.getStatus().isBlank()) {
            supplier.setStatus("ACTIVE");
        }
        supplier = supplierRepository.save(supplier);

        // 3. Tạo quan hệ ShopSupplier
        Shops shopEntity = shopsRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop hiện tại không tồn tại"));

        ShopSupplier shopSupplier = ShopSupplier.builder()
                .shop(shopEntity) // ← ĐÚNG: truyền entity Shops, không phải Long
                .supplier(supplier)
                .currentCostPrice(BigDecimal.ZERO)
                .debtBalance(BigDecimal.ZERO)
                .paymentTermDays(30)
                .note(null)
                .status("ACTIVE")
                .build();

        shopSupplierRepository.save(shopSupplier);

        return mapper.toResponse(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse update(Long id, SupplierRequest request) {
        Long shopId = getCurrentShopId();

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại"));

        // Validate unique toàn cục khi update
        if (!supplier.getCode().equals(request.getCode())
                && supplierRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Mã nhà cung cấp đã tồn tại");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !request.getEmail().equals(supplier.getEmail())
                && supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email nhà cung cấp đã tồn tại");
        }

        mapper.updateEntityFromRequest(request, supplier);
        supplier = supplierRepository.save(supplier);

        // ShopSupplier không thay đổi gì ở update supplier global
        // Nếu cần update giá nhập/công nợ → làm API riêng cho ShopSupplier

        return mapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse getById(Long id) {
        Long shopId = getCurrentShopId();

        // Kiểm tra shop có quan hệ với supplier này không
        if (!shopSupplierRepository.existsByShopIdAndSupplierId(shopId, id)) {
            throw new RuntimeException("Bạn không có quyền xem nhà cung cấp này");
        }

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại"));

        return mapper.toResponse(supplier);
    }

    @Override
    public Page<SupplierResponse> getList(String search, Pageable pageable) {
        Long shopId = getCurrentShopId();

        Page<ShopSupplier> page;

        if (search != null && !search.trim().isEmpty()) {
            page = shopSupplierRepository.findByShopIdAndSupplierNameContainingIgnoreCase(shopId, search.trim(), pageable);
        } else {
            page = shopSupplierRepository.findSupplierIdsByShopId(shopId, pageable);
        }

        if (page.isEmpty()) {
            return Page.empty(pageable);
        }

        List<SupplierResponse> responses = page.getContent().stream()
                .map(shopSupplier -> {
                    SupplierResponse dto = mapper.toResponse(shopSupplier.getSupplier());

                    // Bổ sung thông tin riêng theo shop từ ShopSupplier
                    dto.setCurrentCostPrice(shopSupplier.getCurrentCostPrice());
                    dto.setDebtBalance(shopSupplier.getDebtBalance());
                    dto.setPaymentTermDays(shopSupplier.getPaymentTermDays());
                    dto.setNote(shopSupplier.getNote());

                    return dto;
                })
                .toList();

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long shopId = getCurrentShopId();

        // Kiểm tra quan hệ tồn tại
        ShopSupplier shopSupplier = shopSupplierRepository.findByShopIdAndSupplierId(shopId, id)
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không thuộc shop của bạn"));

        // Kiểm tra công nợ = 0 trước khi xóa (an toàn)
        if (shopSupplier.getDebtBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Không thể xóa nhà cung cấp đang có công nợ");
        }

        // Xóa quan hệ shop-supplier
        shopSupplierRepository.delete(shopSupplier);

        // Nếu không còn shop nào dùng supplier này → xóa global
        if (!shopSupplierRepository.existsBySupplierId(id)) {
            supplierRepository.deleteById(id);
        }
    }
}