package com.SWP391_G5_EventFlowerExchange.LoginAPI.repository;


import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetail;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
    List<OrderDetail> findByOrder_OrderID(int orderID);
    List<OrderDetail> findByFlowerBatchIn(List<FlowerBatch> flowerBatches);
    @Query("SELECT o.flowerBatch, SUM(o.quantity) as totalQuantity FROM OrderDetail o " +
            "GROUP BY o.flowerBatch " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findMostPurchasedFlowerBatch();

}
