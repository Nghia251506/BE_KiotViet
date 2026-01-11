package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.Product.ProductRequest;
import com.example.be_kiotviet.dto.Product.ProductResponse;
import com.example.be_kiotviet.entity.Category;
import com.example.be_kiotviet.entity.Product;
import com.example.be_kiotviet.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Autowired
    CategoryRepository categoryRepository = null;

    @Mapping(target = "category", source = "categoryId") // map id → entity
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product entity);
    default Category mapCategoryId(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
    }
}
