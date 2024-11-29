
package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostingCreationRequest {
    String title;
    String description;
    BigDecimal price;
    String imageUrl;
    String status;
    LocalDate createdAt;
    LocalDate updatedAt;
    Integer userID;
}

