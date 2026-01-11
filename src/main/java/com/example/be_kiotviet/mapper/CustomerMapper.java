package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.Customer.CustomerRequest;
import com.example.be_kiotviet.dto.Customer.CustomerResponse;
import com.example.be_kiotviet.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    Customer toEntity(CustomerRequest customerRequest);
    CustomerResponse toResponse(Customer entity);
    void updateEntity (@MappingTarget Customer entity, CustomerRequest request);
}
