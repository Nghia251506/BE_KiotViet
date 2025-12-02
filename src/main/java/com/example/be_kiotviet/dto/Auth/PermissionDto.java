package com.example.be_kiotviet.dto.Auth;

import lombok.Data;

@Data
public class PermissionDto {
    private Long id;
    private String code;
    private String name;
    private String description;
}
