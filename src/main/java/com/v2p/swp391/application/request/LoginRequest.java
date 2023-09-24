package com.v2p.swp391.application.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequest implements Serializable {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}
