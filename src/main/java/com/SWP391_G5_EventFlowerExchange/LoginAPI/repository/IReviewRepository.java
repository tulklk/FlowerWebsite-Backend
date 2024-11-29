package com.SWP391_G5_EventFlowerExchange.LoginAPI.repository;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByFlowerBatch_FlowerID(int flowerID);
    boolean existsById(Integer id);

}

