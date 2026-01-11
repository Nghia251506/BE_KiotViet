package com.example.be_kiotviet.dto.Customer;

import lombok.Data;

@Data
public class CustomerResponse {
    private String id;
    private String name;
    private String email;
    private String taxCode;
    private String phone;
    private String packageType;
}
