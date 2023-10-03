package com.v2p.swp391.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bird_pairing")
public class BirdPairing extends BaseEntity{

    @JoinColumn(name = "new_bird_id")
    private Bird newBird;

    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @Column(name = "status")
    private String status;
}
