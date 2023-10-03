package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bird_pairing")
public class BirdPairing extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "new_bird_id")
    private Bird newBird;

    @ManyToOne
    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @Column(name = "status")
    private String status;
}
