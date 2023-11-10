package com.v2p.swp391.application.response;

import com.v2p.swp391.application.model.Order;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class OrderPaymentRespone {
    private Long orderId;
    private Order order;

    private PaymentRespone paymentRespone;
}
