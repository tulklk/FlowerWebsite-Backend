package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackDTO {
    int feedbackID;
    String comment;
    int rating;
    User user;
}
