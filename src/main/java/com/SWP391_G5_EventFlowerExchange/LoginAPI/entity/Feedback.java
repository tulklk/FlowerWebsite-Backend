package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int feedbackID;

    @Column(nullable = false, length = 1000)
    String comment; // Feedback comment

    @Column(nullable = false)
    int rating; // Rating out of 5

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    User user; // The user who created the feedback

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = true)
    LocalDateTime responseAt; // When the response was given
    String response; // Response from admin

    @Column(nullable = false)
    boolean anonymous = false; // If feedback is anonymous

    @Column(nullable = false)
    int likeCount = 0; // Number of likes for the feedback

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
