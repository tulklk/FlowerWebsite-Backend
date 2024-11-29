package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryUpdateRequest {
    String availableStatus;
}
