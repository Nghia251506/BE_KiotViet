package com.example.be_kiotviet.mapper.Auth;


import com.example.be_kiotviet.dto.Auth.RoleDto;
import com.example.be_kiotviet.entity.Permission;
import com.example.be_kiotviet.entity.Role;

import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDto toDto(Role role) {
        if (role == null) return null;

        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setShopId(role.getShopId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());

        if (role.getPermissions() != null) {
            dto.setPermissions(
                    role.getPermissions()
                            .stream()
                            .map(Permission::getCode)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }
}
