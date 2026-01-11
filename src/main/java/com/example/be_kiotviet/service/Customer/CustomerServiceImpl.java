package com.example.be_kiotviet.service.Customer;

import com.example.be_kiotviet.dto.Customer.CustomerRequest;
import com.example.be_kiotviet.dto.Customer.CustomerResponse;
import com.example.be_kiotviet.entity.Customer;
import com.example.be_kiotviet.exception.ResourceNotFoundException;
import com.example.be_kiotviet.mapper.CustomerMapper;
import com.example.be_kiotviet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements  CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper  customerMapper;
    @Override
    public CustomerResponse create(CustomerRequest customerRequest) {
        if(customerRequest.getName() == null)
        {
            throw new RuntimeException("Vui lòng nhập tên");
        }
        if(customerRequest.getPhone() == null)
        {
            throw new RuntimeException("Vui lòng nhập số điện thoại");
        }
        if(customerRequest.getEmail() == null)
        {
            throw new RuntimeException("Vui lòng nhập địa chỉ email");
        }
        Customer entity = customerMapper.toEntity(customerRequest);
        entity = customerRepository.save(entity);
        return customerMapper.toResponse(entity);
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest customerRequest) {
        Customer entity =  customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        customerMapper.updateEntity(entity, customerRequest);
        entity = customerRepository.save(entity);
        return customerMapper.toResponse(entity);
    }
    @Override
    public Page<CustomerResponse> getAll(Pageable pageable){return customerRepository.findAll(pageable).map(customerMapper::toResponse);}
    @Override
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }
        customerRepository.deleteById(id);
    }
}
