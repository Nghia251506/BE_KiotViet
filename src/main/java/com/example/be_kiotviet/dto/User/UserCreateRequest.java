package com.example.be_kiotviet.dto.User;

import lombok.Data;

@Data
public class UserCreateRequest {
    private Long shopId;
    private String username;
    private String password;    // sẽ hash lưu vào password_hash
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
}
