package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;


import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.OrderDetailRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.ApiResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.OrderDetailResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.*;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.FlowerBatchService;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.IOrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {

    IOrderDetailService orderDetailService;
    FlowerBatchService flowerBatchService;

    private ApiResponse<OrderDetail> createOrderDetail(Order order, OrderDetailRequest detailRequest) {
        FlowerBatch flowerBatch = flowerBatchService.findById(detailRequest.getFlowerID());

        // Check if the flower batch exists
        if (flowerBatch == null) {
            return ApiResponse.<OrderDetail>builder()
                    .code(404) // Not found
                    .message("Flower not found for ID: " + detailRequest.getFlowerID())
                    .build();
        }

        // Check if the requested quantity is available
        if (detailRequest.getQuantity() > flowerBatch.getQuantity()) {
            return ApiResponse.<OrderDetail>builder()
                    .code(400) // Bad request
                    .message("Insufficient quantity for flower ID: " + detailRequest.getFlowerID())
                    .build();
        }

        // Create and set OrderDetail properties
        OrderDetail orderDetail = new OrderDetail();
        OrderDetailKey orderDetailKey = new OrderDetailKey();

        orderDetailKey.setOrderID(order.getOrderID());
        orderDetailKey.setFlowerID(detailRequest.getFlowerID());

        orderDetail.setId(orderDetailKey);
        orderDetail.setOrder(order); // Associate with the created order
        orderDetail.setFlowerBatch(flowerBatch); // Reference the flower batch
        orderDetail.setQuantity(detailRequest.getQuantity());
        orderDetail.setPrice(detailRequest.getPrice()); // Price for the order detail

        // Update flower quantity after order detail is created
        flowerBatchService.updateFlowerQuantity(detailRequest.getFlowerID(),
                flowerBatch.getQuantity() - detailRequest.getQuantity());

        // Save the order detail
        orderDetailService.createOrderDetail(orderDetail);

        return ApiResponse.<OrderDetail>builder()
                .result(orderDetail)
                .code(1000) // Success code
                .message("Order detail created successfully")
                .build();
    }


    @GetMapping("/")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getOrderDetail(@PathVariable OrderDetailKey id) {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable OrderDetailKey id, @RequestBody OrderDetail orderDetail) {
        return ResponseEntity.ok(orderDetailService.updateOrderDetail(id, orderDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable OrderDetailKey id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("OrderDetail deleted!");
    }

    // Get Order Details by Order ID
    @GetMapping("/by-order/{orderID}")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetailsByOrderID(@PathVariable int orderID) {
        List<OrderDetailResponse> orderDetailsResponses = orderDetailService.getOrderDetailsByOrderID(orderID);
        return ResponseEntity.ok(orderDetailsResponses);
    }

    // Get Seller by Order ID
    @GetMapping("/orders-by-seller/{sellerID}")
    public ResponseEntity<List<OrderDetailResponse>> getOrdersBySellerID(@PathVariable int sellerID) {
        List<OrderDetailResponse> orders = orderDetailService.getOrdersBySellerID(sellerID);
        return ResponseEntity.ok(orders);
    }
}

