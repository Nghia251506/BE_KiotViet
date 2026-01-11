package com.example.be_kiotviet.dto.Product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellPrice;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private String unit;
    private BigDecimal weight;
    private BigDecimal volume;
    private Boolean isActive;
    private Long categoryId;
    private String extraAttributes;
    private String tags;
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private List<ProductImageResponse> images;
    private List<ProductSupplierResponse> suppliers;

    // getters setters
}
