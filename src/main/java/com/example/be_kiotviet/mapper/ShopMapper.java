package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.entity.Customer;
import com.example.be_kiotviet.entity.Shops;
import com.example.be_kiotviet.repository.CustomerRepository;
import com.example.be_kiotviet.repository.ShopRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShopMapper {
    @Autowired
    ShopRepository shopsRepository = null;
    CustomerRepository customerRepository = null;
    // BỎ HOÀN TOÀN DÒNG INSTANCE ĐI
    // ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);
    @Mapping(target = "code", source = "code")
    @Mapping(target = "businessType", source = "businessType")
    @Mapping(target = "businessDescription", source = "businessDescription")
    @Mapping(target = "customer",source = "customerId")
    @Mapping(target = "isHeadquarterFlag", source = "headquarter")
    @Mapping(target = "parentHeadquarter", source = "headquarterId")
    Shops toEntity(ShopRequest request);

    @Mapping(target = "defaultUsername", source = "phone")
    @Mapping(target = "defaultPassword", constant = "123456")
    ShopResponse toResponse(Shops entity);

    void updateEntity(@MappingTarget Shops entity, ShopRequest request);

// METHOD CUSTOM: Map Long → Shops (tìm theo ID)
    default Shops mapHeadquarterIdToEntity(Long headquarterId) {
        if (headquarterId == null) {
            return null;
        }
        return shopsRepository.findById(headquarterId)
                .orElseThrow(() -> new RuntimeException("Trụ sở chính không tồn tại với ID: " + headquarterId));
    }
    default Customer mapCustomerIdToEntity(Long customerId) {
        if (customerId == null) {
            return null;
        }
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer không tồn tại"));
    }
}
