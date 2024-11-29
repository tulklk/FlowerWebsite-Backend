package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;

import java.util.List;
import java.util.Optional;

public interface IFlowerBatchService {
    FlowerBatch updateFlowerQuantity(int flowerID, int newQuantity);
    List<FlowerBatch> getAllFlowerBatch();
    List<FlowerBatch> insertFlowerBatch(List<FlowerBatch> flowerBatchList);
    FlowerBatch updateFlowerBatch(int flowerID, FlowerBatch flowerBatch);
    void deleteFlowerBatch(int flowerID);
    Optional<FlowerBatch> getFlowerBatchById(int flowerID);
    FlowerBatch createFlower(FlowerBatch flowerBatch, int postId);
    FlowerBatch findById(int flowerID);
}
