package com.example.be_kiotviet.dto.Product;

import lombok.Data;

@Data
public class ProductImageResponse {
    private Long id;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean isPrimary;
}
