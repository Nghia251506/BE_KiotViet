package com.example.be_kiotviet.controller;

import com.example.be_kiotviet.dto.Supplier.SupplierRequest;
import com.example.be_kiotviet.dto.Supplier.SupplierResponse;
import com.example.be_kiotviet.service.Supplier.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<?> getList(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierResponse> result = supplierService.getList(search, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", result.getContent());
        response.put("currentPage", result.getNumber());
        response.put("totalItems", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * Tạo nhà cung cấp mới (global) + tự động tạo quan hệ với shop hiện tại
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SupplierRequest request) {
        try {
            SupplierResponse response = supplierService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Lỗi hệ thống khi tạo nhà cung cấp"));
        }
    }

    /**
     * Cập nhật thông tin nhà cung cấp global
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody SupplierRequest request) {
        try {
            SupplierResponse response = supplierService.update(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Lỗi hệ thống khi cập nhật"));
        }
    }

    /**
     * Lấy chi tiết 1 nhà cung cấp (chỉ nếu shop hiện tại có quan hệ với NCC này)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            SupplierResponse response = supplierService.getById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Xóa nhà cung cấp (xóa quan hệ shop-supplier + xóa global nếu không còn dùng)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            supplierService.delete(id);
            return ResponseEntity.ok(successResponse("Xóa nhà cung cấp thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Lỗi hệ thống khi xóa"));
        }
    }

    // Helper để trả response thống nhất
    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    private Map<String, Object> successResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

}
