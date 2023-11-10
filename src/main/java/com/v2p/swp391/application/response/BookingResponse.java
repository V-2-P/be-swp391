package com.v2p.swp391.application.response;

import com.v2p.swp391.application.model.*;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class BookingResponse {
    //Booking
    private Long bookingId;
    private Booking booking;

    //PaymentRespone
    private PaymentRespone paymentRespone;
}
