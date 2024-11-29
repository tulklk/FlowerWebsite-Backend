package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.FlowerBatchWithQuantity;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Delivery;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Order;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Payment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Order order;
    List<FlowerBatchWithQuantity> flowerBatchesWithQuantity; // List to hold flower batches with quantities
    Delivery delivery;
    Payment payment;
}

