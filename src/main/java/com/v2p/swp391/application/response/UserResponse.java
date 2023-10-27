package com.v2p.swp391.application.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.v2p.swp391.application.model.RoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    @JsonIgnore
    private Long id;

    private String fullName;

    private String email;

    @JsonIgnore
    private String password;

    private String imageUrl;

    private String phoneNumber;

    private String roleEntity;

    private String dob;

}