package com.example.be_kiotviet.dto.User;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private Long shopId;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Set<String> roles;
    private String roleName;
    private Boolean isActive;
}
