package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.ShopSupplier.ShopSupplierRequest;
import com.example.be_kiotviet.dto.ShopSupplier.ShopSupplierResponse;
import com.example.be_kiotviet.entity.ShopSupplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShopSupplierMapper {

    @Mapping(target = "supplier.id", source = "supplierId")
    @Mapping(target = "shop", ignore = true) // shop sẽ set trong service
    ShopSupplier toEntity(ShopSupplierRequest request);

    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierCode", source = "supplier.code")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "supplierEmail", source = "supplier.email")
    @Mapping(target = "supplierPhone", source = "supplier.phone")
    ShopSupplierResponse toResponse(ShopSupplier entity);
}
