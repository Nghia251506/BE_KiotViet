package com.example.be_kiotviet.mapper.Auth;

import com.example.be_kiotviet.dto.Auth.PermissionDto;
import com.example.be_kiotviet.entity.Permission;

public class PermissionMapper {

    public static PermissionDto toDto(Permission p) {
        if (p == null) return null;

        PermissionDto dto = new PermissionDto();
        dto.setId(p.getId());
        dto.setCode(p.getCode());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        return dto;
    }
}
