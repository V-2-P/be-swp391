package com.v2p.swp391.application.request;

import jakarta.validation.constraints.*;
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

    private String description;

    @Min(value = 1, message = "Category id must be greater than or equal to 1")
    private Long categoryId;

    @Min(value = 1, message = "BirdType id must be greater than or equal to 1")
    private Long typeId;

    private boolean status;


    private String thumbnail;

    private String purebredLevel;

    private String competitionAchievements;

    private String age;

    @Min(value = 0, message = "Quantity must be greater than or equal to 1")
    private String quantity;

    @Pattern(regexp = "female|male", message = "Gender must be female or male")
    private String gender;

    private String color;

}





