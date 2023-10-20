package com.v2p.swp391.application.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackBookingUpdateRequest {
    @Length(min = 20)
    private String comment;

    private String rating;
}
