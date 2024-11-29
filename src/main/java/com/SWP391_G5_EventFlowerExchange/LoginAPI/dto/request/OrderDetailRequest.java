package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailRequest {
    int flowerID;  // Assuming you have a FlowerBatch with this ID
    int quantity;
    double price;
}
