package com.example.be_kiotviet.dto.Product;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductSupplierResponse {
    private Long id;
    private Long supplierId;
    private String supplierName; // nếu cần trả name
    private BigDecimal costPrice;
    private String note;
}
