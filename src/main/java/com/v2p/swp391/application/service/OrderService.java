package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.OrderStatus;
import com.v2p.swp391.application.request.CartItemRequest;
import com.v2p.swp391.application.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order, List<CartItemRequest> cartItems) ;
    Order getOrder(Long id);
    Page<OrderResponse> getListOrder(OrderStatus status,String keyword, PageRequest pageRequest);
    Order shippingOrder(Long id, Order order) ;
    Order confirmOrder(Long id);
    Order deliveredOrder(Long id);
    void deleteOrder(Long id);
    List<Order> findByUser();

    List<Order> findByUserId(long userId);
}
