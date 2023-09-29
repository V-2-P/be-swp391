package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirdRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("type_id")
    private Long typeId;


    private boolean status;

    public boolean isStatus() {
        return true;
    }

    @JsonProperty("purebred_level")
    private String purebredLevel;

    @JsonProperty("competition_achievements")
    private int competitionAchievements;

    @JsonProperty("age")
    private String age;

    private int quantity;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("color")
    private String color;



}

