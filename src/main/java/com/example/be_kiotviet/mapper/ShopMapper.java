package com.example.be_kiotviet.mapper;

import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.entity.Shops;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShopMapper {

    // BỎ HOÀN TOÀN DÒNG INSTANCE ĐI
    // ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

    Shops toEntity(ShopRequest request);

    ShopResponse toResponse(Shops entity);

    void updateEntity(@MappingTarget Shops entity, ShopRequest request);
}
