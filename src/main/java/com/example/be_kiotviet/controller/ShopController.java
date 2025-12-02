package com.example.be_kiotviet.controller;

import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.service.Shop.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopResponse create(@Valid @RequestBody ShopRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ShopResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<ShopResponse> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ShopResponse update(@PathVariable Long id, @Valid @RequestBody ShopRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
