package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultiReviewRequest {
    int userID; // User submitting the review
    List<ReviewRequest> flowerReviews; // List of flower reviews
}
