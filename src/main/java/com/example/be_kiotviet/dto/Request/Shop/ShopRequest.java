package com.example.be_kiotviet.dto.Request.Shop;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ShopRequest {
    @NotBlank private String name;
    @NotBlank @Email private String email;
    @Pattern(regexp = "^[a-z0-9-]+$") private String slug;
    private String phone;
    private String domain;
    private String packageType;
}