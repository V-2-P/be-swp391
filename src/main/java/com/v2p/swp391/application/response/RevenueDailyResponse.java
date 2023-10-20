package com.v2p.swp391.application.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueDailyResponse {

    private String day;

    private Float totalPayment;
}
