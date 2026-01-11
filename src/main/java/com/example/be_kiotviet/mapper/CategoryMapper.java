package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.category.CategoryResponseDto;
import com.example.be_kiotviet.entity.Category;

import java.util.ArrayList;

public class CategoryMapper {

    public static CategoryResponseDto toResponseDto(Category category) {
        if (category == null) return null;

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setSortOrder(category.getSortOrder() != null ? category.getSortOrder() : 0);
        dto.setChildren(new ArrayList<>()); // sẽ được fill ở service khi build tree

        return dto;
    }
}