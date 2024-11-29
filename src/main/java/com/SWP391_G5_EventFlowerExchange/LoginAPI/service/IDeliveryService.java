package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.DeliveryCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.DeliveryCreationResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Delivery;

import java.util.List;
import java.util.Optional;

public interface IDeliveryService {
    List<Delivery> getAllDeliveries();
    Delivery addDelivery(Delivery delivery);
    DeliveryCreationResponse insertDelivery(DeliveryCreationRequest request);
    Delivery updateDelivery(int deliveryID,Delivery delivery);
    void deleteDelivery(int deliveryID);
    Optional<Delivery> getDeliveryById(int deliveryID);

    Delivery updateDeliveryStatus(int deliID, String deliveryStatus);
}
