// src/main/java/com/tns/newscrawler/dto/auth/RoleDto.java
package com.example.be_kiotviet.dto.Auth;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDto {
    private Long id;
    private Long shopId;
    private String name;
    private String description;
    // danh sách permission code cho tiện nhìn
    private Set<String> permissions;
}
