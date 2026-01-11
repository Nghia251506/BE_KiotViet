package com.example.be_kiotviet.dto.Customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank private  String name;
    @NotBlank private String phone;
    @NotBlank private String email;
    private String taxCode;
    private String PackageType;
}
