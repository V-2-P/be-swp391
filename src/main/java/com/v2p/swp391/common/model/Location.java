package com.v2p.swp391.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    @JsonProperty("lat")
    private double lat;
    @JsonProperty("long")
    private double lon;
}
