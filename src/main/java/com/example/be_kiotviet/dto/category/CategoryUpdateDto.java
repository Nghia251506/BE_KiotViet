package com.example.be_kiotviet.dto.category;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {

    @Size(max = 255, message = "Tên danh mục không quá 255 ký tự")
    private String name;

    private String description;

    private String imageUrl;

    private Long parentId;

    private Integer sortOrder;
}