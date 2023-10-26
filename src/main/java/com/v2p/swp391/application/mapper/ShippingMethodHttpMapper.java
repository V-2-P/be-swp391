package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.ShippingMethod;
import com.v2p.swp391.application.request.ShippingMethodRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShippingMethodHttpMapper {
    ShippingMethodHttpMapper INSTANCE = Mappers.getMapper(ShippingMethodHttpMapper.class);


    ShippingMethod toModel(ShippingMethodRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateShippingFromRequest(ShippingMethodRequest request, @MappingTarget ShippingMethod shippingMethod);


}
