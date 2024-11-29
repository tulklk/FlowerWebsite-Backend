package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class UserLoginRequest {
    @Email
    @NotBlank
    String email;
    @NotBlank
    String password;
}
