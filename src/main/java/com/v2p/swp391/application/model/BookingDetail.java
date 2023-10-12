package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking_detail")
public class BookingDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "bird_types_id")
    private BirdType birdType;

    @ManyToOne
    @JoinColumn(name = "father_bird_id")
    private Bird fatherBird;

    @ManyToOne
    @JoinColumn(name = "mother_bird_id")
    private Bird motherBird;

    @Column(name = "status")
    private String status;
}
