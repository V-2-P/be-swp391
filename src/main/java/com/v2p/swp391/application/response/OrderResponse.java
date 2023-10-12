package com.v2p.swp391.application.response;


import lombok.*;

import java.time.LocalDate;
import java.util.List;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;

    private Long userId;

    private String fullName;

    private String phoneNumber;

    private String shippingAddress;

    private String note;

    private float totalMoney;

    private float totalPayment;

    private float discount;

    private LocalDate orderDate;

    private String status;

    private String paymentMethod;

    private String shippingMethod;

    private LocalDate shippingDate;

    private String trackingNumber;

    private List<OrderDetailResponse> orderDetails;
}