package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSupplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier; // entity Supplier bạn sẽ tạo (id, name, phone, address...)

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "cost_price", precision = 15, scale = 2)
    private BigDecimal costPrice; // giá nhập từ supplier này

    @Column(name = "note", length = 500)
    private String note; // ghi chú nhà cung cấp

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
