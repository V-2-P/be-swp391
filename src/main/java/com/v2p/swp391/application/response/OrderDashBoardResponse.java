package com.v2p.swp391.application.response;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class OrderDashBoardResponse {
    private String userName;

    private Float amount;

    private String status;

    private Long invoive;

}
