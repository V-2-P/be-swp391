package com.v2p.swp391.application.service.impl;


import com.v2p.swp391.application.mapper.OrderHttpMapper;
import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.request.CartItemRequest;
import com.v2p.swp391.application.response.OrderResponse;
import com.v2p.swp391.application.service.OrderService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BirdRepository birdRepository;
    private final VoucherRepository voucherRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderHttpMapper orderMapper;
    private final UseVoucherRepository useVoucherRepository;

    @Override
    public Order createOrder(Order order, List<CartItemRequest> cartItems) {
        User user = userRepository
                .findById(order.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", order.getUser().getId()));
        order.setUser(user);
        order.setActive(true);

        float totalPayment = 0.0f;

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemRequest cartItemDTO : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Long birdId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Bird bird = birdRepository.findById(birdId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bird", "id", birdId));
            if (bird.getQuantity() < quantity) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Not enough birds in stock.");
            }
            float totalMoney = bird.getPrice() * quantity;
            totalPayment += totalMoney;
            orderDetail.setBird(bird);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(bird.getPrice());
            orderDetails.add(orderDetail);
            order.setTotalMoney(totalMoney);
        }

        UseVoucher useVoucher = new UseVoucher();
        if (order.getVoucher() != null && order.getVoucher().getId() != null) {
            float discount = discount(order, user, totalPayment);
            useVoucher.setUser(user);
            useVoucher.setVoucher(order.getVoucher());
            useVoucher.setCreatedAt(LocalDate.now());
            useVoucherRepository.save(useVoucher);
            totalPayment -= discount;
        }

        order.setTotalPayment(totalPayment);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.pending);
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        useVoucher.setOrder(order);
        orderDetailRepository.saveAll(orderDetails);

        for (OrderDetail orderDetail : orderDetails) {
            Bird bird = orderDetail.getBird();
            int quantity = orderDetail.getNumberOfProducts();
            bird.setQuantity(bird.getQuantity() - quantity);
            birdRepository.save(bird);
        }
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }


    @Override
    public Page<OrderResponse> getListOrder(OrderStatus status, PageRequest pageRequest) {
        Page<Order> orderPage;
        orderPage = orderRepository.findByStatus(status, pageRequest);
        return orderPage.map(orderMapper::toResponse);
    }

    @Override
    public Order shippingOrder(Long id, Order order) {
        Order existingOrder = getOrder(id);
        existingOrder.setShippingMethod(order.getShippingMethod());
        existingOrder.setTrackingNumber(order.getTrackingNumber());
        existingOrder.setShippingDate(LocalDate.now());
        existingOrder.setStatus(OrderStatus.shipping);
        return orderRepository.save(existingOrder);
    }

    @Override
    public Order confirmOrder(Long id) {
        Order existingOrder = getOrder(id);
        existingOrder.setStatus(OrderStatus.processing);
        return orderRepository.save(existingOrder);
    }

    @Override
    public Order deliveredOrder(Long id) {
        Order existingOrder = getOrder(id);
        existingOrder.setStatus(OrderStatus.delivered);
        existingOrder.setActive(false);
        existingOrder.setReceivedDate(LocalDate.now());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        if (OrderStatus.shipping.equals(order.getStatus())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot cancel order with status SHIPPING");
        }
        if (OrderStatus.cancelled.equals(order.getStatus())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Order has been cancelled");
        }
        order.setActive(false);
        order.setStatus(OrderStatus.cancelled);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Bird bird = orderDetail.getBird();
            int quantity = orderDetail.getNumberOfProducts();
            bird.setQuantity(bird.getQuantity() + quantity);
            birdRepository.save(bird);
        }

    }

    @Override
    public List<Order> findByUserId(Long userId) {

        return orderRepository.findByUserId(userId);
    }

    private float discount(Order order, User user, float totalMoney) {
        float discount;

        Long userId = user.getId();
        Long voucherId = order.getVoucher().getId();
        if (useVoucherRepository.existsByUser_IdAndVoucher_Id(userId, voucherId)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "User has already used this voucher.");
        }

        Voucher voucher = voucherRepository
                .findById(order.getVoucher().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Voucher", "id", order.getVoucher().getId()));
        float minValue = voucher.getMinValue();

        if (totalMoney >= minValue) {
            LocalDate currentDate = LocalDate.now();
            if (voucher.getExpirationDate() != null && voucher.getExpirationDate().isBefore(currentDate)) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Voucher has expired.");
            }
            if (voucher.getAmount() > 0) {
                voucher.setAmount(voucher.getAmount() - 1);
                voucherRepository.save(voucher);
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST, "Voucher is out of stock.");
            }
            discount = voucher.getDiscount();
            order.setVoucher(voucher);
        } else {
            throw new AppException(HttpStatus.BAD_REQUEST, "Total money is less than minValue. Cannot use this voucher.");
        }

        return discount;
    }





}
