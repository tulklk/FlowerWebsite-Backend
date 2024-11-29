package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Delivery;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Payment;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    LocalDateTime orderDate;
    double totalPrice;
    String shippingAddress;
    User user;
    Delivery delivery;
    Payment payment;
    List<OrderDetailRequest> orderDetails;
}


