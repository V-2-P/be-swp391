package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "birds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bird extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BirdType birdType;

    @Column(name = "status")
    private boolean status;

    @Column(name = "purebred_level")
    private String purebredLevel;

    @Column(name = "competition_achievements")
    private int competitionAchievements;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "color")
    private String color;

    @Column(name = "quantity")
    private int quantity;
}
