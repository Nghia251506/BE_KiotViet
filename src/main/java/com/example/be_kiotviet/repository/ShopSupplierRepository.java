package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.ShopSupplier;
import org.springframework.data.domain.*;
//import org.springframework.data.domain.PageableDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ShopSupplierRepository extends JpaRepository<ShopSupplier, Long> {

    Optional<ShopSupplier> findByShopIdAndSupplierId(Long shopId, Long supplierId);

    List<ShopSupplier> findByShopId(Long shopId);

    List<ShopSupplier> findBySupplierId(Long supplierId);

    // Lấy danh sách NCC có công nợ > 0 của shop
    List<ShopSupplier> findByShopIdAndDebtBalanceGreaterThan(Long shopId, BigDecimal zero);

    // Lấy danh sách NCC theo trạng thái
    List<ShopSupplier> findByShopIdAndStatus(Long shopId, String status);
    // Lấy ID supplier của shop (cho pagination)
    @Query(value = "SELECT ss.id " +
            "FROM shop_suppliers ss " +
            "JOIN suppliers s ON ss.supplier_id = s.id " +
            "WHERE ss.shop_id = :shopId " +
            "ORDER BY s.name ASC",
            countQuery = "SELECT COUNT(*) FROM shop_suppliers ss WHERE ss.shop_id = :shopId",
            nativeQuery = true)
    Page<ShopSupplier> findSupplierIdsByShopId(@Param("shopId") Long shopId,Pageable pageable);
    boolean existsBySupplierId(Long id);
    boolean existsByShopIdAndSupplierId(Long shopId, Long id);
    @Query(value = "SELECT ss.id " +
            "FROM shop_suppliers ss " +
            "JOIN suppliers s ON ss.supplier_id = s.id " +
            "WHERE ss.shop_id = :shopId " +
            "AND LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "ORDER BY s.name ASC",
            countQuery = "SELECT COUNT(*) FROM shop_suppliers ss JOIN suppliers s ON ss.supplier_id = s.id WHERE ss.shop_id = :shopId AND LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))",
            nativeQuery = true)
    Page<ShopSupplier> findByShopIdAndSupplierNameContainingIgnoreCase(
            @Param("shopId") Long shopId,
            @Param("search") String search,
            Pageable pageable);
}
