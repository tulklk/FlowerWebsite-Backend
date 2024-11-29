package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.enums.PaymentEnums;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentID;

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private PaymentEnums method;

    @Column(nullable = false, length = 255)
    private String status = "pending";
}
