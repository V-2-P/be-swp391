package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "feedback_booking")
public class FeedbackBooking extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Range(min = 1, max = 5)
    private int rating;

    private String comment;

    @Column(name = "is_active")
    private int isActive;
}
