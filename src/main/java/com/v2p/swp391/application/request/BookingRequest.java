package com.v2p.swp391.application.request;

import com.v2p.swp391.application.model.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @Min(value = 1, message = "User's ID must be >0")
    private Long userId;

    @NotBlank(message = "Vui lòng nhập tên người đặt hàng")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Size(min = 10, message = "Số điện thoại chưa đúng định dạng")
    private String phoneNumber;

    @NotBlank(message = "Vui lòng nhập địa chỉ giao hàng")
    private String shippingAddress;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Booking detail is required")
    private BookingDetailRequest bookingDetailRequest;

    private Float shippingMoney;
}
