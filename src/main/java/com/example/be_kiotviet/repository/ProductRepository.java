package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopId(Long shopId, Pageable pageable);
    Page<Product> findByShopIdAndNameContainingIgnoreCase(Long shopId, String name, Pageable pageable);
    Page<Product> findByShopIdAndCategoryId(Long shopId, Long categoryId, Pageable pageable);
    Optional<Product> findByIdAndShopId(Long id, Long shopId); // multi-tenant an toàn
}
