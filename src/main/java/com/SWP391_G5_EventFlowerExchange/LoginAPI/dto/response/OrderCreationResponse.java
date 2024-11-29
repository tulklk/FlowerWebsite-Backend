package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Delivery;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Payment;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationResponse {
    LocalDateTime orderDate;
    double totalPrice;
    String shippingAddress;
    String status = "Đã Thanh Toán";

    User user;
    Delivery delivery;
    Payment payment;
}
