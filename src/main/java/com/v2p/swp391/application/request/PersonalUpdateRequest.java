package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalUpdateRequest {
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;

    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String currentPassword;
    private String password;
    private String confirmedPassword;
}
