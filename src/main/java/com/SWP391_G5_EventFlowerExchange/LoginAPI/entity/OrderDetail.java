package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
public class OrderDetail {

    @EmbeddedId
    private OrderDetailKey id;

    @ManyToOne
    @MapsId("orderID")
    @JoinColumn(name = "orderID")
    private Order order;

    @ManyToOne
    @MapsId("flowerID")
    @JoinColumn(name = "flowerID")
    private FlowerBatch flowerBatch;

    private int quantity;
    private double price;
}
