package com.v2p.swp391.application.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueMonthlyResponse {

    private String period;

    private Float totalPayment;
}
