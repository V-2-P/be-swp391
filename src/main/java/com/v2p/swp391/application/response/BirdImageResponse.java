package com.v2p.swp391.application.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirdImageResponse {
    private int id;
    private String imageUrl;

}
