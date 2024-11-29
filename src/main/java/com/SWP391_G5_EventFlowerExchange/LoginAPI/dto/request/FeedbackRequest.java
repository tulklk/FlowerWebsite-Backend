package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    String name;      // Tên người gửi feedback
    String email;     // Email người gửi feedback
    String comment;   // Nội dung feedback
    int rating;       // Đánh giá (rating)
    boolean anonymous; // Cờ ẩn danh
}


