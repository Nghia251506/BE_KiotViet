package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.ProductSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long> {

    // Lấy tất cả supplier của 1 product
    List<ProductSupplier> findByProductId(Long productId);

    // Lấy theo productId và shopId (multi-tenant)
    List<ProductSupplier> findByProductIdAndShopId(Long productId, Long shopId);

    // Lấy theo supplierId (dùng khi xem chi tiết supplier)
    List<ProductSupplier> findBySupplierId(Long supplierId);

    // Xóa theo productId (dùng cascade nên không bắt buộc)
    void deleteByProductId(Long productId);
}