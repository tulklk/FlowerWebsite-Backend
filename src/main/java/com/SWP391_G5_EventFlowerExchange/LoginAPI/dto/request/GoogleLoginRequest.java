package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleLoginRequest {
    String token; // Using token is sent by Frontend
}
