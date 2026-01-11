package com.example.be_kiotviet.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 255, message = "Tên danh mục không quá 255 ký tự")
    private String name;

    private String description;

    private String imageUrl;

    private Long parentId; // null nếu là danh mục gốc

    private Integer sortOrder;
}