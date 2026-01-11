package com.example.be_kiotviet.dto.Product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class ProductRequest {
    private String name;
    private String description;
    private String barcode;
    private BigDecimal costPrice;
    private BigDecimal sellPrice;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private String unit;
    private BigDecimal weight;
    private BigDecimal volume;
    private Boolean isActive;
    private Long categoryId;
    private String extraAttributes; // JSON string
    private String tags; // JSON string
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;

    private List<String> imageUrls; // list URL ảnh khi tạo (FE upload trước)
    private List<ProductSupplierRequest> suppliers; // list supplier khi tạo

    // getters setters
}
