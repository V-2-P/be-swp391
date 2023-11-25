package com.v2p.swp391.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.OrderDetail;
import com.v2p.swp391.application.model.OrderStatus;
import com.v2p.swp391.application.repository.OrderRepository;
import com.v2p.swp391.common.api.CoreResponse;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.shipment.model.OrderItem;
import com.v2p.swp391.shipment.request.CreateOrderGHNRequest;
import com.v2p.swp391.shipment.response.CreateOrderReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrder implements CreateOrderStrategy<Order>{
    private final OrderRepository orderRepository;

    @Override
    public Order excute(Long id, CreateOrderGHNRequest request,GhnRestTemplate ghnRestTemplate) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

            request.setTo_name(order.getFullName());
            request.setTo_phone(order.getPhoneNumber());
            request.setTo_address(order.getToAddress());
            request.setTo_ward_code(order.getToWardCode());
            request.setTo_district_id(order.getToDistrictId());

            if(order.getPaymentMethod().equals("vnpay")){
                request.setCod_amount(0);
            }else{
                request.setCod_amount(Math.round(order.getTotalPayment()));
            }

            request.setContent("Khách hàng : "+ order.getFullName() + " đặt mua "+ order.getOrderDetails().stream().map(e->e.getBird().getName()).collect(Collectors.joining(", ")));

            request.setHeight(15);
            request.setLength(15);
            request.setWidth(15);
            request.setWeight(5000);

            request.setService_id(order.getServiceId());
            request.setService_type_id(order.getServiceTypeId());

            List<OrderItem> items = new ArrayList<>();
            for (OrderDetail detail : order.getOrderDetails()) {
                Bird bird = detail.getBird();
                OrderItem item = new OrderItem();
                item.setName(bird.getName());
                item.setCode(bird.getId().toString());
                item.setQuantity(detail.getNumberOfProducts());
                item.setPrice(Math.round(detail.getPrice()));
                item.setHeight(15);
                item.setLength(15);
                item.setWidth(15);
                item.setWeight(5000);
                items.add(item);
            }
            request.setItems(items);

            HttpEntity<CreateOrderGHNRequest> requestEntity = new HttpEntity<>(request);
            Object jsonResponse = ghnRestTemplate.postForObject(
                    "/shiip/public-api/v2/shipping-order/create",
                    requestEntity,
                    Object.class
            );

            CoreResponse<CreateOrderReponse> createOrderReponse = convertObjectToValue(jsonResponse,CoreResponse.class);
            CreateOrderReponse createdOrder = convertObjectToValue(createOrderReponse.getData(),CreateOrderReponse.class);

            order.setTrackingNumber(createdOrder.getOrder_code());
            order.setStatus(OrderStatus.shipping);

            orderRepository.save(order);
            return order;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }
    private <T> T convertObjectToValue(Object object, Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, clazz);
    }
}
