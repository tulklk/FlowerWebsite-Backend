package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.FeedbackDTO;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.FeedbackRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Feedback;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Create feedback

    @PostMapping("/create")
    public ResponseEntity<Feedback> addFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        // Lấy thông tin từ FeedbackRequest (bao gồm name, email, comment, rating, anonymous)
        Feedback feedback = feedbackService.saveFeedback(feedbackRequest.getName(),
                feedbackRequest.getEmail(),
                feedbackRequest.getComment(),
                feedbackRequest.getRating(),
                feedbackRequest.isAnonymous());
        return ResponseEntity.ok(feedback);
    }



    // Lấy tất cả feedback
    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<FeedbackDTO> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    // Retrieve feedback by ID
    @GetMapping("/{feedbackID}")
    public ResponseEntity<Feedback> getFeedbackByID(@PathVariable int feedbackID) {
        return feedbackService.getFeedbackByID(feedbackID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Retrieve feedback by user ID
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Feedback>> getFeedbackByUserID(@PathVariable int userID) {
        List<Feedback> feedbackList = feedbackService.getFeedbackByUserID(userID);
        return ResponseEntity.ok(feedbackList);
    }

    // Update feedback
    @PutMapping("/{feedbackID}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable int feedbackID,
                                                   @RequestParam String comment,
                                                   @RequestParam int rating,
                                                   @RequestParam(defaultValue = "false") boolean anonymous) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(feedbackID, comment, rating, anonymous);
            return ResponseEntity.ok(updatedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete feedback
    @DeleteMapping("/{feedbackID}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable int feedbackID) {
        try {
            feedbackService.deleteFeedback(feedbackID);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
