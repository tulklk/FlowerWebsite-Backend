package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailKey implements Serializable {

    @Column(name = "orderID")
    private int orderID;

    @Column(name = "flowerID")
    private int flowerID;

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailKey)) return false;
        OrderDetailKey that = (OrderDetailKey) o;
        return orderID == that.orderID && flowerID == that.flowerID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, flowerID);
    }
}
