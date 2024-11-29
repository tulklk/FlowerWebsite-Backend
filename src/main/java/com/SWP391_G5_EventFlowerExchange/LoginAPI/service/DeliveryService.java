package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.DeliveryCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.DeliveryCreationResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Delivery;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IDeliveryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryService implements IDeliveryService{
    IDeliveryRepository deliveryRepository;

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    public DeliveryCreationResponse insertDelivery(DeliveryCreationRequest request) {
        // Set the current timestamp for deliveryDate
        request.setDeliveryDate(LocalDateTime.now());

        // Convert the request to the Delivery entity
        Delivery delivery = Delivery.builder()
                .deliveryDate(request.getDeliveryDate())
                .rating(request.getRating())
                .availableStatus(request.getAvailableStatus())
                .build();

        // Save the Delivery entity to the database using repository
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // Create and return a DeliveryCreationResponse object with relevant details
        return DeliveryCreationResponse.builder()
                .deliveryDate(savedDelivery.getDeliveryDate())
                .rating(savedDelivery.getRating())
                .availableStatus(savedDelivery.getAvailableStatus())
                .build();
    }


    @Override
    public Delivery updateDelivery(int deliveryID, Delivery delivery) {
        Delivery deli =deliveryRepository.findById(deliveryID)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        if(deli !=null){
            if (deli.getDeliveryDate()!=null){
                deli.setDeliveryDate(delivery.getDeliveryDate());
            }
            if (deli.getAvailableStatus()!=null){
                deli.setAvailableStatus(deli.getAvailableStatus());
            }
            if(deli.getRating()!=0){
                deli.setRating(deli.getRating());
            }
        }
        return deliveryRepository.save(deli);
    }

    @Override
    public void deleteDelivery(int deliveryID) {
            deliveryRepository.deleteById(deliveryID);
    }

    @Override
    public Optional<Delivery> getDeliveryById(int deliveryID) {
        return deliveryRepository.findById(deliveryID);
    }

    @Override
    public Delivery addDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery updateDeliveryStatus(int deliID, String deliveryStatus) {
        Delivery delivery = deliveryRepository.findById(deliID)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + deliID));

        delivery.setAvailableStatus(deliveryStatus);
        return deliveryRepository.save(delivery);
    }
}
