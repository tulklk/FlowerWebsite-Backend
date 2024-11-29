package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, length = 255)
    private String notificationType;

    private LocalDateTime createdAt;  // No default value here

    @ManyToOne
    @JoinColumn(name = "userID", nullable = true)
    private User user;

    // Constructor to initialize createdAt
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
