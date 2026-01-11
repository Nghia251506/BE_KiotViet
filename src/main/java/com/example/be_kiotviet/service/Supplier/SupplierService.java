package com.example.be_kiotviet.service.Supplier;

import com.example.be_kiotviet.dto.Supplier.SupplierRequest;
import com.example.be_kiotviet.dto.Supplier.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {

    SupplierResponse create(SupplierRequest request);

    SupplierResponse update(Long id, SupplierRequest request);

    SupplierResponse getById(Long id);

    Page<SupplierResponse> getList(String search, Pageable pageable);

    void delete(Long id);
}
