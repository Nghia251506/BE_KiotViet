package com.example.be_kiotviet.service.Customer;

import com.example.be_kiotviet.dto.Customer.CustomerRequest;
import com.example.be_kiotviet.dto.Customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Long id, CustomerRequest request);
    Page<CustomerResponse> getAll(Pageable pageable);
    void delete(Long id);
}
