package com.v2p.swp391.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private long id ;

    private Long birdId;

    private String birdName;

    private String thumbnail;

    private String gender;

    private Float price;

    private int numberOfProducts;


}
