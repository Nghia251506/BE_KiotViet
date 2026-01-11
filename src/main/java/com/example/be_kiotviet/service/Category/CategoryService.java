package com.example.be_kiotviet.service.Category;

import com.example.be_kiotviet.dto.category.CategoryCreateDto;
import com.example.be_kiotviet.dto.category.CategoryResponseDto;
import com.example.be_kiotviet.dto.category.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getCategoryTree();

    Page<CategoryResponseDto> getCategoryList(String search, Pageable pageable);

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto createCategory(CategoryCreateDto dto);

    CategoryResponseDto updateCategory(Long id, CategoryUpdateDto dto);

    void deleteCategory(Long id);
}
