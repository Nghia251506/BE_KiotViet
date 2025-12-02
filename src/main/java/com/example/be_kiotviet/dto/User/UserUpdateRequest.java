package com.example.be_kiotviet.dto.User;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private Long shopId;
    private String email;
    private String password;
    private Long roleId;
    private Boolean isActive;
    private String role;
}
