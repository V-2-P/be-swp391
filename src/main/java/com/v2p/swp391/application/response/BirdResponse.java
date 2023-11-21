package com.v2p.swp391.application.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BirdResponse {
    private int id;

    private String name;

    private String categoryName;

    private String birdType;

    private String gender;

    private Float price;

    private String thumbnail;

    private int quantity;




}