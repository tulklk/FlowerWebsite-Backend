package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.FlowerBatchWithQuantity;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.OrderDetailResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.*;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IEventFlowerPostingRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IOrderDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService implements IOrderDetailService {

    IOrderDetailRepository orderDetailRepository;
    IEventFlowerPostingRepository eventFlowerPostingRepository;



    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail getOrderDetail(OrderDetailKey id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
    }

    @Override
    public OrderDetail updateOrderDetail(OrderDetailKey id, OrderDetail orderDetail) {
        OrderDetail existingOrderDetail = getOrderDetail(id);
        existingOrderDetail.setQuantity(orderDetail.getQuantity());
        existingOrderDetail.setPrice(orderDetail.getPrice());
        existingOrderDetail.setFlowerBatch(orderDetail.getFlowerBatch());
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(OrderDetailKey id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailsByOrderID(int orderID) {
        // Fetch all order details for the given order ID
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_OrderID(orderID);

        if (orderDetails.isEmpty()) {
            return Collections.emptyList(); // Handle case where there are no order details
        }

        // Consolidate flower batches into a list along with their quantities
        List<FlowerBatchWithQuantity> flowerBatchesWithQuantity = orderDetails.stream()
                .map(orderDetail -> new FlowerBatchWithQuantity(orderDetail.getFlowerBatch(), orderDetail.getQuantity()))
                .collect(Collectors.toList());

        // Get delivery and payment from the first order detail
        Order order = orderDetails.getFirst().getOrder();
        Delivery delivery = order.getDelivery();
        Payment payment = order.getPayment();

        // Create a single response with all flower batches and their quantities
        OrderDetailResponse response = new OrderDetailResponse(
                order,
                flowerBatchesWithQuantity, // Pass the consolidated flower batches with quantities
                delivery,
                payment
        );

        return Collections.singletonList(response); // Return as a list containing one consolidated response
    }


    @Override
    public List<OrderDetailResponse> getOrdersBySellerID(int sellerID) {
        // Find all postings made by the seller
        List<EventFlowerPosting> postings = eventFlowerPostingRepository.findByUser_UserID(sellerID);

        // Collect all flower batches from the postings
        List<FlowerBatch> flowerBatches = postings.stream()
                .flatMap(posting -> posting.getFlowerBatches().stream())
                .collect(Collectors.toList());

        // Find all order details that reference any of the seller's flower batches
        List<OrderDetail> orderDetails = orderDetailRepository.findByFlowerBatchIn(flowerBatches);

        // Group order details by order ID and create responses
        return orderDetails.stream()
                .collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getOrderID()))
                .values().stream()
                .map(details -> {
                    // Consolidate flower batches for this group of order details
                    List<FlowerBatchWithQuantity> consolidatedFlowerBatchesWithQuantity = details.stream()
                            .map(orderDetail -> new FlowerBatchWithQuantity(orderDetail.getFlowerBatch(), orderDetail.getQuantity()))
                            .collect(Collectors.toList());

                    // Extract delivery and payment details from the first order detail in the group
                    Order order = details.getFirst().getOrder();
                    Delivery delivery = order.getDelivery();
                    Payment payment = order.getPayment();

                    return new OrderDetailResponse(
                            order,
                            consolidatedFlowerBatchesWithQuantity, // Pass the consolidated flower batches with quantities
                            delivery,
                            payment
                    );
                })
                .collect(Collectors.toList());
    }




}
