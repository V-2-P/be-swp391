package com.v2p.swp391.application.response;

import com.v2p.swp391.application.model.Bird;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class BirdSearchResponse {
    private List<Bird> birds;
    private int totalPages;
}
