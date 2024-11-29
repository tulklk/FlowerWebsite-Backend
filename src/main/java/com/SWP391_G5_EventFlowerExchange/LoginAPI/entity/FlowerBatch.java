package com.SWP391_G5_EventFlowerExchange.LoginAPI.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "FlowerBatch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flowerID;

    @Column(nullable = false)
    private String flowerName;

    @Column(nullable = false)
    private int quantity = 1;  // Default value for quantity

    private String status = "available";  // Default value for status

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "postID", nullable = false)
    @JsonBackReference
    private EventFlowerPosting eventFlowerPosting;

    @ManyToOne
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;


    @Override
    public String toString() {
        return "FlowerBatch{" +
                "flowerID=" + flowerID +
                ", flowerName='" + flowerName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlowerBatch)) return false;
        FlowerBatch that = (FlowerBatch) o;
        return flowerID == that.flowerID; // So sánh theo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowerID);
    }
}
