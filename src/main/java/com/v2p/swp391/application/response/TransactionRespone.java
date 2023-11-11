package com.v2p.swp391.application.response;

import com.v2p.swp391.application.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRespone implements Serializable{
    private String status;
    private String message;
    private String data;
    private Payment payment;
}
