package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bird_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirdImage {
    public static final int MAXIMUM_IMAGES_PER_BIRD = 4;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bird_id")
    private Bird bird;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

}
