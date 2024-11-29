package com.SWP391_G5_EventFlowerExchange.LoginAPI.repository;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.EventFlowerPosting;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerImage;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFlowerImageRepository extends JpaRepository<FlowerImage, Integer> {
    List<FlowerImage> findByFlowerBatch(FlowerBatch flowerBatch);
    @Modifying
    @Query("DELETE FROM FlowerImage fi WHERE fi.flowerBatch.flowerID = :flowerID")
    void deleteByFlowerBatchFlowerID(@Param("flowerID") int flowerID);

}
