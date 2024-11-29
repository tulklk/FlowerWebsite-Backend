package com.SWP391_G5_EventFlowerExchange.LoginAPI.exception;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.EventFlowerPosting;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;

public class NotEnoughProductsInStockException  extends Exception{
    private static final String DEFAULT_MESSAGE = "Not enough products in stock";
    public NotEnoughProductsInStockException(){
        super(DEFAULT_MESSAGE);
    }
    public NotEnoughProductsInStockException(FlowerBatch flowerBatch) {
        super(String.format("Not enough %s products in stock. Only %d left", flowerBatch.getFlowerName(), flowerBatch.getQuantity()));
    }
}
