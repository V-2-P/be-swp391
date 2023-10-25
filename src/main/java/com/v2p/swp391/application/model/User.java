package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;

    @Email
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String address;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    private int isActive;
}
