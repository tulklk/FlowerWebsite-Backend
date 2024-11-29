package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryCreationResponse {
    LocalDateTime deliveryDate;
    int rating;
    String availableStatus;
}
