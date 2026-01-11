package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl; // url từ cloud

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false; // ảnh chính
}
