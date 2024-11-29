package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.MultiReviewRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.ReviewRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.ApiResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Review;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")   
    public ResponseEntity<ApiResponse<List<Review>>> createReviews(@RequestBody MultiReviewRequest multiReviewRequest) {
        List<Review> reviews = reviewService.createReviews(multiReviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<List<Review>>builder()
                .result(reviews)
                .code(1000)
                .message("Reviews created successfully")
                .build());
    }

    @GetMapping("/flower/{flowerID}")
    public ResponseEntity<List<Review>> getReviewsByFlowerId(@PathVariable int flowerID) {
        List<Review> reviews = reviewService.getReviewsByFlowerId(flowerID);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewID}")
    public ResponseEntity<ApiResponse<Review>> updateReview(@PathVariable int reviewID, @RequestBody ReviewRequest reviewRequest) {
        Review updatedReview = reviewService.updateReview(reviewID, reviewRequest);
        return ResponseEntity.ok(ApiResponse.<Review>builder()
                .result(updatedReview)
                .code(1000)
                .message("Review updated successfully")
                .build());
    }

    @DeleteMapping("/{reviewID}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable int reviewID) {
        reviewService.deleteReview(reviewID);
        // Chỉ cần một thông báo duy nhất
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Deleted successfully") // Thông báo ngắn gọn hơn
                .build());
    }


}

