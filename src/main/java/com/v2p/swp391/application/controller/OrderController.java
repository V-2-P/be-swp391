package com.v2p.swp391.application.controller;


import com.v2p.swp391.application.model.Order;


import com.v2p.swp391.application.model.OrderStatus;
import com.v2p.swp391.application.request.OrderRequest;
import com.v2p.swp391.application.request.ShippingOrderRequest;
import com.v2p.swp391.application.response.OrderPageResponse;
import com.v2p.swp391.application.response.OrderPaymentRespone;
import com.v2p.swp391.application.response.OrderResponse;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.service.OrderService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.v2p.swp391.application.mapper.OrderHttpMapper.INSTANCE;


@RestController
@RequestMapping("${app.api.version.v1}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("")
    public CoreApiResponse<OrderPaymentRespone> createOrder(
            @Valid @RequestBody OrderRequest request
    ) throws IOException {
        OrderPaymentRespone order = orderService.createOrderHavePayment(INSTANCE.toModel(request),request.getCartItems());
        return CoreApiResponse.success(order ,"Create order successfully");
    }

    @GetMapping("/{id}")
    public CoreApiResponse<OrderResponse> getOrderByUser(@Valid @PathVariable Long id){
        Order order = orderService.getOrder(id);
        return CoreApiResponse.success(INSTANCE.toResponse(order));
    }
    @GetMapping("detail/{id}")
    public CoreApiResponse<Order> getOrder(@Valid @PathVariable Long id){
        Order order = orderService.getOrder(id);
        return CoreApiResponse.success(order);
    }

    @GetMapping("")
    public CoreApiResponse<OrderPageResponse> getListOrders(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        Page<OrderResponse> orderPage = orderService.getListOrder(status,keyword,pageRequest);
        int totalPages= orderPage.getTotalPages();
        List<OrderResponse> orders = orderPage.getContent();
        OrderPageResponse orderPageResponse= new OrderPageResponse();
        orderPageResponse.setTotalPages(totalPages);
        orderPageResponse.setOrderResponses(orders);
        return CoreApiResponse.success(orderPageResponse);
    }

    @GetMapping("/all")
    public CoreApiResponse <List<Order>> getAllOrder(){
        List<Order> orders = orderService.getAllOrder();
        return CoreApiResponse.success(orders);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CoreApiResponse<List<OrderResponse>> getOrdersByUserMe() {
            List<Order> orders = orderService.findByUser();
            return CoreApiResponse.success(INSTANCE.toListResponses(orders));

    }
    @GetMapping("/user/{id}")
    public CoreApiResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable Long id) {
        List<Order> orders = orderService.findByUserId(id);
        return CoreApiResponse.success(INSTANCE.toListResponses(orders));

    }

    @PutMapping("/shipping/{id}")
    public CoreApiResponse<OrderResponse> shippingOrder(
            @PathVariable Long id,
            @Valid @RequestBody ShippingOrderRequest request){
        Order shippingOrder = orderService.shippingOrder(id, INSTANCE.toModelUpdate(request));
        return CoreApiResponse.success(INSTANCE.toResponse(shippingOrder), "Update order successfully");
    }
    @PutMapping("/rePayment/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CoreApiResponse<PaymentRespone> rePayment(
            @PathVariable Long id) throws UnsupportedEncodingException {
        PaymentRespone rePayment = orderService.rePayment(id);
        return CoreApiResponse.success(rePayment, "Payment for order successfully");
    }

    @PutMapping("/confirm/{id}")
    public CoreApiResponse<OrderResponse> confirmOrder(
            @PathVariable Long id){
        Order confirmOrder = orderService.confirmOrder(id);
        return CoreApiResponse.success(INSTANCE.toResponse(confirmOrder), "Confirm order successfully");
    }
    @PutMapping("/delivered/{id}")
    public CoreApiResponse<OrderResponse> deliveredOrder(
            @PathVariable Long id){
        Order deliveredOrder = orderService.deliveredOrder(id);
        return CoreApiResponse.success(INSTANCE.toResponse(deliveredOrder), "Update order successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return CoreApiResponse.success("Cancel order with id: " + id + " successfully");
    }




}
