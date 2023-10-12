package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.OrderStatus;
import com.v2p.swp391.application.request.CartItemRequest;
import com.v2p.swp391.application.request.ShippingOrderRequest;
import com.v2p.swp391.application.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order, List<CartItemRequest> cartItems) ;
    Order getOrder(Long id);
    Page<OrderResponse> getListOrder(String status, PageRequest pageRequest);
    Order shippingOrder(Long id, Order order) ;
    Order confirmOrder(Long id);
    Order deliveredOrder(Long id);
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
