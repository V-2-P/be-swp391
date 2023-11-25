package com.v2p.swp391.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.BookingRepository;
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
public class CreateBooking implements CreateOrderStrategy<Booking>{
    private final BookingRepository bookingRepository;
    @Override
    public Booking excute(Long id, CreateOrderGHNRequest request,GhnRestTemplate ghnRestTemplate) {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

            request.setTo_name(booking.getFullName());
            request.setTo_phone(booking.getPhoneNumber());
            request.setTo_address(booking.getToAddress());
            request.setTo_ward_code(booking.getToWardCode());
            request.setTo_district_id(booking.getToDistrictId());

            if(booking.getPaymentMethod() == PaymentMethod.Debit_Or_Credit_Card){
                request.setCod_amount(0);
            }else{
                request.setCod_amount(Math.round(booking.getTotalPayment()));
            }

            request.setContent("Khách hàng : "+ booking.getFullName() + " đặt lai chim "+ booking.getBookingDetail().getFatherBird().getName() + " và "+ booking.getBookingDetail().getMotherBird().getName());

            request.setHeight(15);
            request.setLength(15);
            request.setWidth(15);
            request.setWeight(5000);

            request.setService_id(booking.getServiceId());
            request.setService_type_id(booking.getServiceTypeId());

            List<OrderItem> items = new ArrayList<>();
            for (BirdPairing birdPairing : booking.getBookingDetail().getBirdPairing()) {
                Bird bird = birdPairing.getNewBird();
                OrderItem item = new OrderItem();
                item.setName(bird.getName());
                item.setCode(bird.getId().toString());
                item.setQuantity(1);
                item.setPrice(0);
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

            booking.setTrackingNumber(createdOrder.getOrder_code());
            booking.setStatus(BookingStatus.Shipping);

            bookingRepository.save(booking);
            return booking;
        }catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }
    private <T> T convertObjectToValue(Object object, Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, clazz);
    }
}
