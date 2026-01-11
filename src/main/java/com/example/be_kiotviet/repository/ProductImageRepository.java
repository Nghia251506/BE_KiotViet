package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    // Lấy tất cả ảnh của 1 product (sắp xếp theo sortOrder)
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);

    // Lấy ảnh theo productId và shopId (multi-tenant an toàn)
    List<ProductImage> findByProductIdAndShopId(Long productId, Long shopId);

    // Lấy ảnh chính (isPrimary = true) của product
    List<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);

    // Xóa tất cả ảnh của product (dùng cascade trong entity nên không cần, nhưng có để tiện)
    void deleteByProductId(Long productId);
}
