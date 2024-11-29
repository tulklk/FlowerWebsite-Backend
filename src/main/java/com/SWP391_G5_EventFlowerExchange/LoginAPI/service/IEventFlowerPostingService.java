package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.PostingCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.EventFlowerPosting;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IEventFlowerPostingService {
//    public List<FlowerBatch> getAllFlowerBatch();
//    public FlowerBatch insertFlowerBatch(FlowerBatch flowerBatch);
//    public FlowerBatch updateFlowerBatch(int flowerID, FlowerBatch flowerBatch);
//    public void deleteFlowerBatch(int flowerID);
//    public Optional<FlowerBatch> getFlowerBatchById(int flowerID);
public List<EventFlowerPosting> getAllPostsSortedByCreatedAt();
public EventFlowerPosting insertEventFlowerPosting(EventFlowerPosting eventFlowerPosting);
public EventFlowerPosting updateEventFlowerPosting(int postId,EventFlowerPosting eventFlowerPosting);
public void deleteEventFlowerPosting(int postId);
public Optional<EventFlowerPosting> getEventFlowerPostingById(int postId);
public boolean deleteFlowerBatch(int postID, int flowerID);
public List<EventFlowerPosting> searchByKeyword(String keyword);
public List<EventFlowerPosting> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) ;
public EventFlowerPosting createPostByID(int userID, PostingCreationRequest request);
}
