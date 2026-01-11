package com.example.be_kiotviet.dto.ShopSupplier;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopSupplierResponse {

    private Long id;

    // Thông tin supplier (global)
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;

    // Thông tin riêng theo shop
    private BigDecimal currentCostPrice;
    private BigDecimal debtBalance; // công nợ hiện tại
    private Integer paymentTermDays;
    private String note;
    private String status;

    private LocalDateTime cooperationStartDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
