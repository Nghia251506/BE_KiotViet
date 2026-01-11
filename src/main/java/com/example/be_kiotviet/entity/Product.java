package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products",
        indexes = {
                @Index(name = "idx_shop_id", columnList = "shop_id"),
                @Index(name = "idx_category_id", columnList = "category_id"),
                @Index(name = "idx_barcode", columnList = "barcode"),
                @Index(name = "idx_name", columnList = "name")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"barcode", "shop_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // category_id

    @Column(name = "barcode", length = 100)
    private String barcode;

    @Column(name = "cost_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal costPrice; // giá nhập

    @Column(name = "sell_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal sellPrice; // giá bán

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 10;

    @Column(name = "unit", length = 50)
    private String unit = "cái"; // đơn vị

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight; // trọng lượng (g hoặc kg)

    @Column(name = "volume", precision = 10, scale = 2)
    private BigDecimal volume; // thể tích (ml hoặc l)

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "seo_title", length = 255)
    private String seoTitle;

    @Column(name = "seo_description", length = 500)
    private String seoDescription;

    @Column(name = "seo_keywords", length = 500)
    private String seoKeywords; // "t-shirt, thời trang nam, áo thun"

    @Column(name = "extra_attributes", columnDefinition = "JSON")
    private String extraAttributes; // JSON cho attribute đặc thù: {"ram": "16GB", "cpu": "i7"} hoặc {"size": "M,L,XL", "color": "red,blue"}

    @Column(name = "tags", columnDefinition = "JSON")
    private String tags; // JSON mảng tag: ["hot", "new", "sale"]

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Quan hệ với hình ảnh (nhiều ảnh)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    // Quan hệ với supplier (trung gian)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSupplier> suppliers = new ArrayList<>();
}
