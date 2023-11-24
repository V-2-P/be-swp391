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
    @Size(min = 3, max = 200, message = "Tên chim phải từ 3 đến 200 kí tự")
    private String name;

    @Min(value = 0, message = "Giá của chim phải lớn hơn 0")
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


    private String gender;

    private String color;

}





