package com.v2p.swp391.application.mapper;


import com.v2p.swp391.application.model.Voucher;

import com.v2p.swp391.application.request.VoucherRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface VoucherHttpMapper {
    VoucherHttpMapper INSTANCE = Mappers.getMapper(VoucherHttpMapper.class);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "status", target = "status")
    Voucher toModel(VoucherRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "status", target = "status")
    void updateVoucherFromRequest(VoucherRequest request, @MappingTarget Voucher voucher);
}
