package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.OrderDetailResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetail;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetailKey;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetail orderDetail);
    List<OrderDetail> getAllOrderDetails();
    OrderDetail getOrderDetail(OrderDetailKey id);
    OrderDetail updateOrderDetail(OrderDetailKey id, OrderDetail orderDetail);
    void deleteOrderDetail(OrderDetailKey id);
    List<OrderDetailResponse> getOrderDetailsByOrderID(int orderID);
    List<OrderDetailResponse> getOrdersBySellerID(int sellerID);
}
