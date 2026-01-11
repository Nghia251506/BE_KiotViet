package com.example.be_kiotviet.service.Product;

import com.example.be_kiotviet.dto.Product.ProductRequest;
import com.example.be_kiotviet.dto.Product.ProductResponse;
import com.example.be_kiotviet.dto.Product.ProductSupplierRequest;
import com.example.be_kiotviet.entity.Product;
import com.example.be_kiotviet.entity.ProductImage;
import com.example.be_kiotviet.entity.ProductSupplier;
import com.example.be_kiotviet.entity.Supplier;
import com.example.be_kiotviet.mapper.ProductMapper;
import com.example.be_kiotviet.repository.ProductImageRepository;
import com.example.be_kiotviet.repository.ProductRepository;
import com.example.be_kiotviet.repository.ProductSupplierRepository;
import com.example.be_kiotviet.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository; // nếu có
    private final ProductSupplierRepository supplierRepository; // nếu có
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Long shopId = TenantContext.getCurrentShopId();

        Product entity = mapper.toEntity(request);
        entity.setShopId(shopId);

        // Xử lý nhiều hình ảnh
        if (request.getImageUrls() != null) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                String url = request.getImageUrls().get(i);
                ProductImage image = ProductImage.builder()
                        .product(entity)
                        .shopId(shopId)
                        .imageUrl(url)
                        .sortOrder(i)
                        .isPrimary(i == 0)
                        .build();
                entity.getImages().add(image);
            }
        }

        // Xử lý supplier trung gian
        if (request.getSuppliers() != null) {
            for (ProductSupplierRequest supReq : request.getSuppliers()) {
                Supplier supplier = supplierRepository.findById(supReq.getSupplierId())
                        .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại")).getSupplier();

                ProductSupplier sup = ProductSupplier.builder()
                        .product(entity)
                        .shopId(shopId)
                        .supplier(supplier)
                        .costPrice(supReq.getCostPrice())
                        .note(supReq.getNote())
                        .build();
                entity.getSuppliers().add(sup);
            }
        }

        entity = productRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Long shopId = TenantContext.getCurrentShopId();
        Product entity = productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Update field
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setCostPrice(request.getCostPrice());
        entity.setSellPrice(request.getSellPrice());
        // ... update các field khác tương tự

        // Update hình ảnh: Xóa cũ + thêm mới
        entity.getImages().clear();
        if (request.getImageUrls() != null) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                ProductImage image = ProductImage.builder()
                        .product(entity)
                        .shopId(shopId)
                        .imageUrl(request.getImageUrls().get(i))
                        .sortOrder(i)
                        .isPrimary(i == 0)
                        .build();
                entity.getImages().add(image);
            }
        }

        // Update supplier: Tương tự
        entity.getSuppliers().clear();
        if (request.getSuppliers() != null) {
            for (ProductSupplierRequest supReq : request.getSuppliers()) {
                Supplier supplier = supplierRepository.findById(supReq.getSupplierId())
                        .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại")).getSupplier();

                ProductSupplier sup = ProductSupplier.builder()
                        .product(entity)
                        .shopId(shopId)
                        .supplier(supplier)
                        .costPrice(supReq.getCostPrice())
                        .note(supReq.getNote())
                        .build();
                entity.getSuppliers().add(sup);
            }
        }

        entity = productRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public ProductResponse getById(Long id) {
        Long shopId = TenantContext.getCurrentShopId();
        Product entity = productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        return mapper.toResponse(entity);
    }

    @Override
    public Page<ProductResponse> getList(String search, Long categoryId, Pageable pageable) {
        Long shopId = TenantContext.getCurrentShopId();

        Page<Product> page;
        if (categoryId != null) {
            page = productRepository.findByShopIdAndCategoryId(shopId, categoryId, pageable);
        } else if (search != null && !search.isBlank()) {
            page = productRepository.findByShopIdAndNameContainingIgnoreCase(shopId, search, pageable);
        } else {
            page = (Page<Product>) productRepository.findByShopId(shopId, pageable);
        }

        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long shopId = TenantContext.getCurrentShopId();
        Product entity = productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        productRepository.delete(entity);
    }
}
