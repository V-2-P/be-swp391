package com.v2p.swp391.application.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.v2p.swp391.application.model.RoleEntity;
import com.v2p.swp391.application.model.User;
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
public class UserResponse extends User {
    private int orderQuantity = 0;
    private int bookingQuantity = 0;
}