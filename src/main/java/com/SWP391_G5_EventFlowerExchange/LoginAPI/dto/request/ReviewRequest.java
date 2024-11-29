package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
   int userID; // User submitting the review
   int flowerID; // Flower being reviewed
   int rating; // Rating given
   String comment; // Feedback comment
   int deliveryRating;
}

