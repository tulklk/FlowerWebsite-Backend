package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EventFlowerPosting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFlowerPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int postID;

    String title;

    String description;

    BigDecimal price;

    String imageUrl;
    String status="Còn Hàng";

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;
    @Column(nullable = true )
    LocalDateTime expiryDate; // Ngày hết hạn của bài đăng
    @Transient // Không lưu trường này vào cơ sở dữ liệu
    int expiryDays = 7; // Số ngày mặc định để hết hạn


    @OneToMany(mappedBy = "eventFlowerPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<FlowerBatch> flowerBatches = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    User user;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        if (expiryDate == null) {
            expiryDate = createdAt.plusDays(expiryDays); // Sử dụng giá trị expiryDays
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
