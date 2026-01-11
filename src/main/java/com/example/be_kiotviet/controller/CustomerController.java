package com.example.be_kiotviet.controller;

import com.example.be_kiotviet.dto.Customer.CustomerRequest;
import com.example.be_kiotviet.dto.Customer.CustomerResponse;
import com.example.be_kiotviet.entity.Customer;
import com.example.be_kiotviet.service.Customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping()
    public Page<CustomerResponse> getAll(Pageable pageable) {
        return customerService.getAll(pageable);
    }
    @PostMapping()
    public CustomerResponse create(CustomerRequest customerRequest) {
        return customerService.create(customerRequest);
    }
    @PutMapping("/{id}")
    public CustomerResponse update(Long id, CustomerRequest customerRequest) {
        return customerService.update(id, customerRequest);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }
}
