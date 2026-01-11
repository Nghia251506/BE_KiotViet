package com.example.be_kiotviet.dto.Auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String shopCode;
    private String username;
    private String password;
}
