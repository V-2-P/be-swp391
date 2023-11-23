package com.v2p.swp391.payment.respond;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VnpayResponse {
    private String RspCode;
    private String Message;
}
