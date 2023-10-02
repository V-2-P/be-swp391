package com.v2p.swp391.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.v2p.swp391.application.model.BirdImage;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BirdDetailResponse {
    private int id;

    private String name;

    private String categoryName;

    private String birdType;

    private String gender;

    private Float price;

    private int quantity;

    private String thumbnail;

    private int competitionAchievements;

    private String purebredLevel;

    private String description;

    @JsonProperty("bird_images")
    private List<BirdImage> birdImages = new ArrayList<>();;
}
