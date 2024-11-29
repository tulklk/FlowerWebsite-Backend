package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;


import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.FlowerBatchWithQuantity;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.EventFlowerPosting;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.ResourceNotFoundException;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IEventFlowerPostingRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IFlowerBatchRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IOrderDetailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlowerBatchService implements IFlowerBatchService{

    IFlowerBatchRepository flowerBatchRepository;
    IEventFlowerPostingRepository eventFlowerPostingRepository;
    IOrderDetailRepository orderDetailRepository;

    @Override
    public FlowerBatch findById(int flowerID) {
        Optional<FlowerBatch> flowerBatch = flowerBatchRepository.findById(flowerID);
        return flowerBatch.orElseThrow(() -> new EntityNotFoundException("FlowerBatch not found with ID: " + flowerID));
    }

    @Override
    public FlowerBatch updateFlowerQuantity(int flowerID, int newQuantity) {
        FlowerBatch flowerBatch = findById(flowerID); // Retrieve the FlowerBatch using the existing method

        // Update the quantity
        flowerBatch.setQuantity(newQuantity); // Assuming you have a setQuantity method in FlowerBatch

        // Save the updated FlowerBatch back to the repository
        return flowerBatchRepository.save(flowerBatch);
    }


    @Override
    public List<FlowerBatch> getAllFlowerBatch() {
        return flowerBatchRepository.findAll();
    }

    @Override
    public List<FlowerBatch> insertFlowerBatch(List<FlowerBatch> flowerBatchList) {
        return flowerBatchRepository.saveAll(flowerBatchList);
    }
    @Override
    public FlowerBatch updateFlowerBatch(int flowerID, FlowerBatch flowerBatch) {
        FlowerBatch fb=flowerBatchRepository.getById(flowerID);
        if(fb != null){
            if (flowerBatch.getFlowerName() != null) {
                fb.setFlowerName(flowerBatch.getFlowerName());
            }
            if (flowerBatch.getQuantity() != 1) {
                fb.setQuantity(flowerBatch.getQuantity());
            }
            if (flowerBatch.getStatus() != null) {
                fb.setStatus(flowerBatch.getStatus());
            }
            if (flowerBatch.getDescription() != null) {
                fb.setDescription(flowerBatch.getDescription());
            }
            if (flowerBatch.getPrice() != null) {
                fb.setPrice(flowerBatch.getPrice());
            }
//            if (flowerBatch.getImageUrl() != null) {
//                fb.setImageUrl(flowerBatch.getImageUrl());
//            }
            if (flowerBatch.getCategory() != null) {
                fb.setCategory(flowerBatch.getCategory());
            }
            if (flowerBatch.getEventFlowerPosting() != null) {
                fb.setEventFlowerPosting(flowerBatch.getEventFlowerPosting());
            }
            flowerBatchRepository.save(fb);
        }
        return null;
    }

    @Override
    public void deleteFlowerBatch(int flowerID) {
            flowerBatchRepository.deleteById(flowerID);
    }

    @Override
    public Optional<FlowerBatch> getFlowerBatchById(int flowerID) {
        return flowerBatchRepository.findById(flowerID);
    }

    @Override
    public FlowerBatch createFlower(FlowerBatch flowerBatch, int postId) {
        // Tìm EventFlowerPosting bằng postId
        EventFlowerPosting eventFlowerPosting = eventFlowerPostingRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("EventFlowerPosting not found with id: " + postId));

        // Gán eventFlowerPosting cho flowerBatch
        flowerBatch.setEventFlowerPosting(eventFlowerPosting);

        // Lưu flowerBatch mới vào repository
        return flowerBatchRepository.save(flowerBatch);
    }
    public List<FlowerBatchWithQuantity> getMostPurchasedFlowerBatches() {
        List<Object[]> result = orderDetailRepository.findMostPurchasedFlowerBatch();
        return result.stream()
                .map(row -> {
                    FlowerBatch flowerBatch = (FlowerBatch) row[0];
                    int orderQuantity = ((Number) row[1]).intValue(); // Convert the total quantity to int

                    return new FlowerBatchWithQuantity(flowerBatch, orderQuantity);
                })
                .collect(Collectors.toList());
    }


}
