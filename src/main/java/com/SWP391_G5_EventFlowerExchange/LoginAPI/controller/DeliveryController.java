package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.DeliveryCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.DeliveryUpdateRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.ApiResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.DeliveryCreationResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Delivery;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.IDeliveryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/delivery")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryController {
    IDeliveryService deliveryService;

    // Get All Delivery Company
    @GetMapping("/")
    public ResponseEntity<List<Delivery>> getAllDeliveries(){
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    // Create Delivery Company
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DeliveryCreationResponse> createDelivery(@RequestBody DeliveryCreationRequest request) {
        return ApiResponse.<DeliveryCreationResponse>builder()
                .result(deliveryService.insertDelivery(request))
                .code(201) // Set success code
                .message("Delivery Company created successfully") // Set success message
                .build();
    }

    // Update Delivery Company
    @PutMapping("/{deliID}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable int deliID, @RequestBody Delivery delivery) {
        Delivery updateDeli = deliveryService.updateDelivery(deliID, delivery);
        return ResponseEntity.ok(updateDeli);
    }

    // Delete Delivery Company
    @DeleteMapping("/{deliID}")
    public ResponseEntity<String> deleteDelivery(@PathVariable int deliID) {
        deliveryService.deleteDelivery(deliID);
        return ResponseEntity.ok("Deleted!");
    }

    // Get Delivery Company by their ID
    @GetMapping("/{deliID}")
    public ResponseEntity<Optional<Delivery>> getDeliveryById(@PathVariable int deliID) {
        Optional<Delivery> deli= deliveryService.getDeliveryById(deliID);
        return ResponseEntity.ok(deli);
    }

    @PatchMapping("/status/{deliID}")
    public ResponseEntity<Delivery> updateDeliveryStatus(@PathVariable int deliID, @RequestBody DeliveryUpdateRequest request) {
        Delivery updatedDelivery = deliveryService.updateDeliveryStatus(deliID, request.getAvailableStatus());
        return ResponseEntity.ok(updatedDelivery);
    }
}
