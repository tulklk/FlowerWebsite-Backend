package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

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
public class OrderRequest {
    LocalDateTime orderDate;
    double totalPrice;
    String shippingAddress;

    User user;
    Payment payment;
    Delivery delivery;

}

