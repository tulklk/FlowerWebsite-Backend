package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.OrderCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.OrderDetailRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.ApiResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.MonthlyRevenueResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Order;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetail;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetailKey;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IOrderRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.*;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;
    OrderDetailService orderDetailService;
    FlowerBatchService flowerBatchService;
    IOrderRepository orderRepository;

    // Create a new order
    @PostMapping("/")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        try {
            // Validate order
            if (order == null || order.getUser() == null || order.getTotalPrice() <= 0) {
                return ResponseEntity.badRequest().body("Invalid order details."); // Return 400 Bad Request
            }

            // Create the order
            Order createdOrder = orderService.insertOrder(order);
            String vnPayURL = "";

            // Generate payment URL if applicable
            if (order.getPayment().getPaymentID() == 1) {
                vnPayURL = orderService.createVNPayUrl(createdOrder);
                System.out.println("VNPay URL: " + vnPayURL);
            }

            return ResponseEntity.status(201).body(vnPayURL); // Return 201 Created with URL
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Order not found."); // Return 404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid argument: " + e.getMessage()); // Return 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage()); // Return 500 Internal Server Error
        }
    }

    @PostMapping("/create")
    public ApiResponse<Order> createNewOrder(@RequestBody OrderCreationRequest request) throws Exception {
        // Step 1: Create the order first
        Order order = orderService.createOrder(request);
        // Step 2: Create order details based on the request
        for (OrderDetailRequest detailRequest : request.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            OrderDetailKey orderDetailKey = new OrderDetailKey();

            orderDetailKey.setOrderID(order.getOrderID());
            orderDetailKey.setFlowerID(detailRequest.getFlowerID());

            orderDetail.setId(orderDetailKey);
            orderDetail.setOrder(order); // Associate with the created order
            orderDetail.setFlowerBatch(flowerBatchService.findById(detailRequest.getFlowerID())); // Fetch FlowerBatch by ID
            orderDetail.setQuantity(detailRequest.getQuantity());
            orderDetail.setPrice(detailRequest.getPrice());

            // Update flower quantity after order detail is created
            flowerBatchService.updateFlowerQuantity(detailRequest.getFlowerID(),
                    orderDetail.getFlowerBatch().getQuantity() - detailRequest.getQuantity());

            orderDetailService.createOrderDetail(orderDetail); // Save order detail
        }

        // Step 3: Check payment method
        String paymentURL = "";
        if (request.getPayment().getPaymentID() == 1) {
            // VNPay payment
            paymentURL = orderService.createVNPayUrl(order); // Generate VNPay URL
            order.setLinkPayment(paymentURL);
        } else if (request.getPayment().getPaymentID() == 2) {
            // MoMo payment
            paymentURL = orderService.createMoMoUrl(order); // Generate MoMo URL
        }

        // Step 4: Return the response with payment URL (if applicable)
        return ApiResponse.<Order>builder()
                .result(order)
                .code(1000) // Set success code
                .message(paymentURL.isEmpty() ? "Create Order Successfully" : "Create Order Successfully. Payment URL: " + paymentURL) // Include payment URL in the message if available
                .build();
    }

    // Handle payment success
    // Handle payment success for both MoMo and VNPay
    @PostMapping("/payments/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam Map<String, String> params) {
        try {
            // Kiểm tra loại thanh toán dựa trên các tham số đặc trưng
            if (params.containsKey("resultCode")) { // Thanh toán MoMo
                String resultCode = params.get("resultCode");
                String txnRef = params.get("orderId");

                if ("0".equals(resultCode)) {
                    orderService.updateOrderStatus(Integer.parseInt(txnRef), "Thành công");
                    String reviewPageUrl = "http://localhost:3000/review/" + txnRef;
                    return ResponseEntity.ok(reviewPageUrl);
                } else if ("1006".equals(resultCode)) {
                    orderService.updateOrderStatus(Integer.parseInt(txnRef), "Chưa thanh toán");
                    return ResponseEntity.ok("Payment was canceled. Order status updated.");
                } else {
                    orderService.updateOrderStatus(Integer.parseInt(txnRef), "Thất bại");
                    return ResponseEntity.ok("Payment failed. Order status updated.");
                }
            } else if (params.containsKey("vnp_ResponseCode")) { // Thanh toán VNPay
                String responseCode = params.get("vnp_ResponseCode");
                String transactionStatus = params.get("vnp_TransactionStatus");
                String txnRef = params.get("vnp_TxnRef");

                if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
                    orderService.updateOrderStatus(Integer.parseInt(txnRef), "Thành công");
                    String reviewPageUrl = "http://localhost:3000/review/" + txnRef;
                    orderService.setLinkPaymentToNull(Integer.parseInt(txnRef));
                    return ResponseEntity.ok(reviewPageUrl);
                } else if ("24".equals(responseCode) && "02".equals(transactionStatus)) {
                    orderService.updateOrderStatus(Integer.parseInt(txnRef), "Chưa thanh toán");
                    return ResponseEntity.ok("Payment was canceled. Order status updated.");
                } else {
                    orderService.updateOrderStatus(Integer.parseInt(txnRef), "Thất bại");
                    return ResponseEntity.ok("Payment failed. Order status updated.");
                }
            }

            return ResponseEntity.badRequest().body("Invalid payment response.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid transaction reference.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }


    // Retrieve all orders
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Retrieve order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));
            return ResponseEntity.ok(order);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Return 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Return 500 Internal Server Error
        }
    }

    // Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable int id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Return 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Return 500 Internal Server Error
        }
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable int id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Order deleted successfully!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Order not found."); // Return 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage()); // Return 500 Internal Server Error
        }
    }

    // Cancel payment
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelPayment(@PathVariable int orderId) {
        try {
            orderService.cancelPayment(orderId);
            return ResponseEntity.ok("Payment canceled successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Order not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while canceling the payment.");
        }
    }
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueResponse>> getMonthlyRevenue() {
        List<MonthlyRevenueResponse> monthlyRevenue = orderService.calculateMonthlyRevenue();
        return ResponseEntity.status(HttpStatus.OK).body(monthlyRevenue);
    }
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Order>> getOrdersByUserID(@PathVariable int userID) {
        List<Order> orders = orderService.getOrdersByUserID(userID);
        return ResponseEntity.ok(orders);
    }


}
