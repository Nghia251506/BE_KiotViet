package com.example.be_kiotviet.controller;

import com.example.be_kiotviet.dto.category.CategoryCreateDto;
import com.example.be_kiotviet.dto.category.CategoryResponseDto;
import com.example.be_kiotviet.dto.category.CategoryUpdateDto;
import com.example.be_kiotviet.service.Category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryResponseDto>> getTree() {
        List<CategoryResponseDto> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getList(
            @RequestParam(required = false) String search,
            @PageableDefault(sort = "sortOrder") Pageable pageable) {

        Page<CategoryResponseDto> page = categoryService.getCategoryList(search, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id) {
        CategoryResponseDto dto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryCreateDto dto) {
        CategoryResponseDto created = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDto dto) {

        CategoryResponseDto updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}