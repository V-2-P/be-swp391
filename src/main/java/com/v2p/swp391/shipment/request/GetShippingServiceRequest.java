package com.v2p.swp391.shipment.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetShippingServiceRequest {
    @JsonIgnore
    private int shop_id;
    @JsonIgnore
    private int from_district;
    private int to_district;
}
