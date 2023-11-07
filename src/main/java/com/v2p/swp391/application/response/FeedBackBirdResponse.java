package com.v2p.swp391.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackBirdResponse {
    private Long id;

    private Long birdId;

    private String userImage;

    private String birdName;

    private int rating;

    private String comment;

    private String birdType;

    private Long orderId;

    private String fullName;

    private String address;

    private String phoneNumber;

    private boolean active;

    private LocalDate createdAt;

}
