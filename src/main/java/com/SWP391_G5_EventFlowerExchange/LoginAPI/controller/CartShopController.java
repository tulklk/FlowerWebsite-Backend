package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.NotEnoughProductsInStockException;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.CartShopService;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.FlowerBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/cart")
public class CartShopController {
    private final CartShopService cartShopService;
    private final FlowerBatchService flowerBatchService;

    @Autowired
    public CartShopController(CartShopService cartShopService, FlowerBatchService flowerBatchService) {
        this.cartShopService = cartShopService;
        this.flowerBatchService = flowerBatchService;
    }

    @GetMapping("/shoppingCart")
    public Map<String, Object> getShoppingCart() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", cartShopService.getFlowerBatchInCart());
        response.put("total", cartShopService.getTotal().toString());
        return response;
    }

    @PostMapping("/shoppingCart/addProduct/{flowerID}")
    public ResponseEntity<Map<String, Object>> addProductToCart(@PathVariable("flowerID") int flowerID) {
        flowerBatchService.getFlowerBatchById(flowerID).ifPresent(cartShopService::addPost);

        Map<String, Object> response = new HashMap<>();
        response.put("products", cartShopService.getFlowerBatchInCart());
        response.put("total", cartShopService.getTotal().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/shoppingCart/removeProduct/{flowerID}")
    public Map<String, Object> removeProductFromCart(@PathVariable("flowerID") int flowerID) {
        flowerBatchService.getFlowerBatchById(flowerID).ifPresent(cartShopService::removeFlowerBatch);
        return getShoppingCart();
    }

    @GetMapping("/shoppingCart/checkout")
    public Map<String, Object> checkout() {
        Map<String, Object> response = new HashMap<>();
        try {
            cartShopService.checkout(); // Thực hiện checkout
            response.put("status", "success");
            response.put("products", cartShopService.getFlowerBatchInCart()); // Lấy lại danh sách sản phẩm
            response.put("total", cartShopService.getTotal().toString()); // Tính tổng
            cartShopService.clearCart();
        } catch (NotEnoughProductsInStockException e) {
            response.put("status", "failed");
            response.put("message", e.getMessage()); // Gửi thông báo lỗi nếu có
            response.put("products", cartShopService.getFlowerBatchInCart()); // Lấy danh sách sản phẩm nếu cần
            response.put("total", cartShopService.getTotal().toString()); // Cập nhật tổng giá trị
        }
        return response; // Trả về phản hồi
    }

    @PostMapping("/shoppingCart/addProduct")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam("flowerID") int flowerID,
                                                         @RequestParam("quantity") int quantity) {
        cartShopService.addToCart(flowerID, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("products", cartShopService.getFlowerBatchInCart());
        response.put("total", cartShopService.getTotal().toString());

        return ResponseEntity.ok(response);
    }
    // xóa toàn bộ Cart
    @PostMapping("/shoppingCart/clearCart")
    public Map<String, Object> clearCart() {
        cartShopService.clearCart();
        return getShoppingCart();
    }
}
