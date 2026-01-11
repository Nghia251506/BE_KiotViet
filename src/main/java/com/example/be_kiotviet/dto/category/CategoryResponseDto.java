package com.example.be_kiotviet.dto.category;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer sortOrder;

    private List<CategoryResponseDto> children = new ArrayList<>();
}