package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shops, Long> {
    boolean existsBySlug(String slug);
    Optional<Shops> findBySlug(String slug);
}