package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Order;

import com.v2p.swp391.application.model.OrderDetail;
import com.v2p.swp391.application.request.OrderRequest;
import com.v2p.swp391.application.request.ShippingOrderRequest;

import com.v2p.swp391.application.response.OrderDetailResponse;
import com.v2p.swp391.application.response.OrderResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper(componentModel = "spring")

public interface OrderHttpMapper {
    OrderHttpMapper INSTANCE = Mappers.getMapper(OrderHttpMapper.class);
    @Mapping(source = "voucherId", target = "voucher.id")
    @Mapping(source = "userId", target = "user.id")
    Order toModel(OrderRequest request);

    Order toModelUpdate(ShippingOrderRequest order);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "voucher.discount", target = "discount")
    OrderResponse toResponse(Order order);


    @Mapping(source = "bird.id", target = "bird")
    @Mapping(source = "orderDetails", target = "orderDetails")
    List<OrderDetailResponse> toOrderResponses(List<OrderDetail> orderDetails);

    @Mapping(source = "bird.id", target = "birdId")
    @Mapping(source = "bird.name", target = "birdName")
    @Mapping(source = "bird.thumbnail", target = "thumbnail")
    @Mapping(source = "bird.gender", target = "gender")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    List<OrderResponse> toListResponses(List<Order> orders);



}

