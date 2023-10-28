package com.v2p.swp391.application.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetPasswordByForgotRequest {
    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Confirmed password is required")
    private String confirmedPassword;
}
