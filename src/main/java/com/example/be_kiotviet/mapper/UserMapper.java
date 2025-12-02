package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.User.UserDto;
import com.example.be_kiotviet.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setShopId(user.getShopId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setIsActive(user.getIsActive());

        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getName());
        }

        return dto;
    }
}
