package com.v2p.swp391.payment.respond;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VnpayResponse {

    @JsonProperty("RspCode")
    private String RspCode;

    @JsonProperty("Message")
    private String Message;
}
