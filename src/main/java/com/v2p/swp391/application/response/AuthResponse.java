package com.v2p.swp391.application.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String refreshToken;
    private String accessToken;
    private Long userId;
    private String role;
    private String imageUrl;

}
