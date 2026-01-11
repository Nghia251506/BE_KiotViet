package com.example.be_kiotviet.dto.Supplier;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String contactPerson;
    private String note;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal currentCostPrice;
    private BigDecimal debtBalance; // công nợ hiện tại
    private Integer paymentTermDays;
}
