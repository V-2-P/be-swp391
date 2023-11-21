package com.v2p.swp391.application.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetShippingServiceRequest {
    private int shop_id;
    private int from_district;
    private int to_district;
}
