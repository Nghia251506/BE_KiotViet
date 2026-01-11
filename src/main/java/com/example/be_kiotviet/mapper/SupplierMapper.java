package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.Supplier.SupplierRequest;
import com.example.be_kiotviet.dto.Supplier.SupplierResponse;
import com.example.be_kiotviet.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplierMapper {

    Supplier toEntity(SupplierRequest request);

    SupplierResponse toResponse(Supplier entity);

    void updateEntityFromRequest(SupplierRequest request, @MappingTarget Supplier entity);
}