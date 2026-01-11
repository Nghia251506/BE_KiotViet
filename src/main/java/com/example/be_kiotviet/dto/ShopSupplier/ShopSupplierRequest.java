package com.example.be_kiotviet.dto.ShopSupplier;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopSupplierRequest {

    @NotNull(message = "ID nhà cung cấp không được để trống")
    private Long supplierId;

    @NotNull(message = "Giá nhập hiện tại không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá nhập phải lớn hơn 0")
    private BigDecimal currentCostPrice;

    @Min(value = 0, message = "Hạn thanh toán không được âm")
    private Integer paymentTermDays = 30;

    private String note;

    private String status; // ACTIVE, INACTIVE – mặc định ACTIVE ở entity
}
