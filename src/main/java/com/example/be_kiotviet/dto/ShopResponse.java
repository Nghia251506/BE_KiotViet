package com.example.be_kiotviet.dto;

import com.example.be_kiotviet.entity.BusinessType;
import com.example.be_kiotviet.entity.Customer;
import com.example.be_kiotviet.entity.Shops;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ShopResponse {
    private Long id;
    private String name;
    private String code;
    private String slug;
    private String email;
    private String phone;
    private String domain;
    private String status;
    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime verificationTokenExpiry;
    private BusinessType businessType;
    private String businessDescription;
    private Customer customer;
    private boolean headquarter;
    private Long headquarterId;
    private String defaultUsername;
    private String defaultPassword;
}
