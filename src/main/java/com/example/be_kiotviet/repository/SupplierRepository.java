package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByCode(String code);

    boolean existsByEmail(String email);

    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Supplier> findByCode(String code);

    Page<Supplier> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Supplier> findAll(Pageable pageable);
}