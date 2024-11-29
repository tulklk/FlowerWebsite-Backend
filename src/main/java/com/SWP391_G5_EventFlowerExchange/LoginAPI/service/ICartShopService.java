package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.NotEnoughProductsInStockException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ICartShopService {
    public void addPost(FlowerBatch flowerBatch);
    public void removeFlowerBatch(FlowerBatch flowerBatch);
    public List<Map<String, Object>> getFlowerBatchInCart();
    void checkout() throws NotEnoughProductsInStockException;
    BigDecimal getTotal();
    public void addToCart(int flowerID, int quantity);
    public void clearCart();
}
