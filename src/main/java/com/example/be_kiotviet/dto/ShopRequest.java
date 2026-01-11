package com.example.be_kiotviet.dto;
import com.example.be_kiotviet.entity.BusinessType;
import com.example.be_kiotviet.entity.Customer;
import com.example.be_kiotviet.entity.Shops;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ShopRequest {
    @NotBlank private String name;
    @NotBlank private String code;
    @NotBlank @Email private String email;
    @Pattern(regexp = "^[a-z0-9-]+$") private String slug;
    private String phone;
    private String domain;
    private BusinessType businessType;
    private String businessDescription;
    private Long customerId;
    private boolean headquarter;
    private Long headquarterId;
}