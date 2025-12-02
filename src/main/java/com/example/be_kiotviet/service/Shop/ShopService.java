package com.example.be_kiotviet.service.Shop;

import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopService {
    ShopResponse create(ShopRequest request);
    ShopResponse update(Long id, ShopRequest request);
    ShopResponse getById(Long id);
    Page<ShopResponse> getAll(Pageable pageable);
    void delete(Long id);
}
