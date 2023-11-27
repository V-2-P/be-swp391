package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.util.List;

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
    @JsonIgnore
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
    @Enumerated(EnumType.STRING)
    private BookingDetailStatus status;

    @OneToMany(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    private List<BirdPairing> birdPairing;
}
