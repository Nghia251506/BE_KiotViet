package com.example.be_kiotviet.service.Product;

import com.example.be_kiotviet.dto.Product.ProductRequest;
import com.example.be_kiotviet.dto.Product.ProductResponse;
import org.springframework.data.domain.*;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    ProductResponse getById(Long id);
    Page<ProductResponse> getList(String search, Long categoryId, Pageable pageable);
    void delete(Long id);
}
