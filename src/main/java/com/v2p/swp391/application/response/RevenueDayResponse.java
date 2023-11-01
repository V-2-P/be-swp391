package com.v2p.swp391.application.response;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class RevenueDayResponse {

    private String day;

    private double totalPayment;
}
