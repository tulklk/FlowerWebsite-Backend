package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.NotEnoughProductsInStockException;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IFlowerBatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CartShopService implements ICartShopService{
    private final IFlowerBatchRepository flowerBatchRepository;
    private Map<FlowerBatch,Integer> flower=new HashMap<>();
    @Autowired
    public CartShopService(IFlowerBatchRepository flowerBatchRepository) {
        this.flowerBatchRepository = flowerBatchRepository;
    }
    /**
     * If product is in the map just increment quantity by 1.
     * If product is not in the map with, add it with quantity 1
     *
     * @paramproduct
     **/

    @Override
    public void addPost(FlowerBatch flowerBatch) {
        System.out.println("Thêm sản phẩm vào giỏ hàng: " + flowerBatch.getFlowerName()); // Log để kiểm tra
        if (flower.containsKey(flowerBatch)) {
            flower.replace(flowerBatch, flower.get(flowerBatch) + 1);
        } else {
            flower.put(flowerBatch, 1);
        }
    }
    /**
     * If product is in the map with quantity > 1, just decrement quantity by 1.
     * If product is in the map with quantity 1, remove it from map
     *
     * @paramproduct
     */
    @Override
    public void removeFlowerBatch(FlowerBatch flowerBatch) {
        if (flower.containsKey(flowerBatch)) {
            if(flower.get(flowerBatch)>1){
                flower.replace(flowerBatch,flower.get(flowerBatch)-1);
            } else if (flower.get(flowerBatch)==1) {
                flower.remove(flowerBatch);
            }
        }
    }
    /**
     * @return unmodifiable copy of the map
     */

    @Override
    public List<Map<String, Object>> getFlowerBatchInCart() {
        List<Map<String, Object>> products = new ArrayList<>();
        System.out.println("Số lượng sản phẩm trong giỏ hàng: " + flower.size()); // Kiểm tra số lượng sản phẩm

        for (Map.Entry<FlowerBatch, Integer> entry : flower.entrySet()) {
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("flowerID", entry.getKey().getFlowerID());
            productDetails.put("flowerName", entry.getKey().getFlowerName());
            productDetails.put("quantity", entry.getValue());
            productDetails.put("price", entry.getKey().getPrice());
//            productDetails.put("imageUrl", entry.getKey().getImageUrl());
            products.add(productDetails);
        }
        return products;
    }
    /**
     * Checkout will rollback if there is not enough of some product in stock
     *
     * @throwsNotEnoughProductsInStockException
     */
    @Override
    public void checkout() throws NotEnoughProductsInStockException {
        if (flower.isEmpty()) {
            throw new NotEnoughProductsInStockException(new FlowerBatch()); // Gọi hàm khởi tạo với một đối tượng FlowerBatch mặc định
        }

        FlowerBatch flowerBatch;
        for (Map.Entry<FlowerBatch, Integer> entry : flower.entrySet()) {
            flowerBatch = flowerBatchRepository.findById(entry.getKey().getFlowerID())
                    .orElseThrow(() -> new NotEnoughProductsInStockException(entry.getKey()));

            if (flowerBatch.getQuantity() < entry.getValue()) {
                throw new NotEnoughProductsInStockException(entry.getKey()); // Ném ngoại lệ với thông tin flowerBatch
            }

            entry.getKey().setQuantity(flowerBatch.getQuantity() - entry.getValue());
        }

        flowerBatchRepository.saveAll(flower.keySet());
        flowerBatchRepository.flush();
//        flower.clear();
    }

    @Override
    public BigDecimal getTotal() {
        return flower.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce( BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public void addToCart(int flowerID, int quantity) {
        FlowerBatch flowerBatch = flowerBatchRepository.findById(flowerID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flower ID: " + flowerID));

        // Check if the requested quantity exceeds available stock
        if (quantity > flowerBatch.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for flower ID: " + flowerID);
        }

        // Add flower batch to cart
        if (flower.containsKey(flowerBatch)) {
            flower.replace(flowerBatch, flower.get(flowerBatch) + quantity);
        } else {
            flower.put(flowerBatch, quantity);
        }
    }

    @Override
    public void clearCart() {
        flower.clear();
    }
}
