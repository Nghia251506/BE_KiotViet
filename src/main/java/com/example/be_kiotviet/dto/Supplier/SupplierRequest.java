package com.example.be_kiotviet.dto.Supplier;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = "Mã nhà cung cấp không được để trống")
    @Size(max = 50, message = "Mã nhà cung cấp không quá 50 ký tự")
    private String code;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Size(max = 255, message = "Tên không quá 255 ký tự")
    private String name;

    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không quá 255 ký tự")
    private String email;

    @Pattern(regexp = "^$|^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Size(max = 500, message = "Địa chỉ không quá 500 ký tự")
    private String address;

    @Size(max = 255, message = "Người liên hệ không quá 255 ký tự")
    private String contactPerson;

    private String note; // không bắt buộc

    private String status; // ACTIVE, INACTIVE – mặc định ACTIVE ở entity
}
