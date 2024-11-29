package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, max = 20, message = "USERNAME_INVALID")
    String username;
    String email;
    @Size(min= 5, message = "PASSWORD_INVALID")
    String password;
    String address;
    String phoneNumber;
    LocalDate createdAt;
}
