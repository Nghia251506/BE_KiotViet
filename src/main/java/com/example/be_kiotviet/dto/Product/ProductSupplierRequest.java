package com.example.be_kiotviet.dto.Product;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductSupplierRequest {
    private Long supplierId;
    private BigDecimal costPrice;
    private String note;
}
