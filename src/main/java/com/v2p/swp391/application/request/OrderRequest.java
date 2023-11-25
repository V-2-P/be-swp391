package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @JsonProperty("userId")
    @Min(value = 1, message = "User's ID must be > 0")
    private Long userId;

    @JsonProperty("fullName")
    @NotBlank(message = "Vui lòng tên người nhận hàng")
    private String fullName;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Size(min = 10, message = "Số điện thoại chưa đúng định dạng")
    private String phoneNumber;

    private String note;

    @JsonProperty("toAddress")
    @NotBlank(message = "Vui lòng nhập địa chỉ giao hàng")
    private String toAddress;

    @JsonProperty("paymentMethod")
    @NotBlank(message = "Vui lòng chọn phương thức thanh toán")
    private String paymentMethod;

    @JsonProperty("shippingMoney")
    @Min(value = 1, message = "Vui lòng nhập phí vận chuyển")
    private Float shippingMoney;

    @NotBlank(message = "Vui lòng chọn phương thức vận chuyển")
    private String shippingMethod;

    private String toWardCode;

    private int toDistrictId;

    private int serviceTypeId;

    private int serviceId;


    private LocalDate expectedDate;

    private Long voucherId;

    @JsonProperty("cartItems")
    @NotNull(message = "Giỏ hàng không được trống")
    private List<CartItemRequest> cartItems;

}




