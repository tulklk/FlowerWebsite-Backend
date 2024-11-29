package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerBatchWithQuantity {
    private FlowerBatch flowerBatch; // Flower batch details
    private int orderQuantity; // Corresponding quantity for the batch
}
