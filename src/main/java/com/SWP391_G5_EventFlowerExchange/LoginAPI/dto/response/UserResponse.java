package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int userID;
    String username;
    String email;
    String address;
    String phoneNumber;
    Set<String> roles;
    String Status;
    LocalDateTime createdAt;
}
