package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.OrderStatus;
import com.v2p.swp391.application.request.CartItemRequest;
import com.v2p.swp391.application.response.OrderPaymentRespone;
import com.v2p.swp391.application.response.OrderResponse;
import com.v2p.swp391.application.response.PaymentRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order, List<CartItemRequest> cartItems) ;
    OrderPaymentRespone createOrderHavePayment(Order order, List<CartItemRequest> cartItems) throws UnsupportedEncodingException;
    PaymentRespone payUnpaidOrder(Long id) throws UnsupportedEncodingException;
    Order getOrder(Long id);
    Page<OrderResponse> getListOrder(OrderStatus status,String keyword, PageRequest pageRequest);
    Order shippingOrder(Long id, Order order) ;
    Order confirmOrder(Long id);
    PaymentRespone rePayment(Long id) throws UnsupportedEncodingException;
    Order deliveredOrder(Long id);
    void cancelledOrder(Long id);
    List<Order> findByUser();
    List<Order> getAllOrder();
    List<Order> findByUserId(long userId);

    Order getOrderByIdForUser(Long id);
}
