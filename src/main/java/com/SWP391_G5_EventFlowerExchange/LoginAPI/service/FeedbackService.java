package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;


import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.FeedbackDTO;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Feedback;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.FeedbackRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackService {


    FeedbackRepository feedbackRepository;

    IUserRepository userRepository;

    // Create or save new feedback
    public Feedback saveFeedback(String name, String email, String comment, int rating, boolean anonymous) {
        // Kiểm tra xem email có tồn tại trong hệ thống hay không
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();  // Lấy đối tượng User từ Optional

            // Tạo feedback với các thông tin
            Feedback feedback = Feedback.builder()
                    .user(user)  // Liên kết feedback với user
                    .comment(comment)
                    .rating(rating)
                    .anonymous(anonymous)
                    .build();

            // Lưu feedback vào database và trả về
            return feedbackRepository.save(feedback);
        } else {
            // Nếu không tìm thấy người dùng với email đó, bạn có thể xử lý tùy theo yêu cầu
            throw new RuntimeException("User not found");
        }
    }


    // Retrieve feedback by user ID
    public List<Feedback> getFeedbackByUserID(int userID) {
        return feedbackRepository.findByUser_UserID(userID);
    }

    // Retrieve feedback by feedback ID
    public Optional<Feedback> getFeedbackByID(int feedbackID) {
        return feedbackRepository.findById(feedbackID);
    }

    // Retrieve all feedback
    public List<FeedbackDTO> getAllFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();

        return feedbackList.stream()
                .map(feedback -> FeedbackDTO.builder()
                        .feedbackID(feedback.getFeedbackID())
                        .comment(feedback.getComment())
                        .rating(feedback.getRating())
                        .user(feedback.getUser())
                        .build())
                .collect(Collectors.toList());
    }


    // Update feedback by ID
    public Feedback updateFeedback(int feedbackID, String comment, int rating, boolean anonymous) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(feedbackID);
        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            feedback.setComment(comment);
            feedback.setRating(rating);
            feedback.setAnonymous(anonymous);
            return feedbackRepository.save(feedback);
        }
        throw new RuntimeException("Feedback not found");
    }

    // Delete feedback by ID
    public void deleteFeedback(int feedbackID) {
        if (feedbackRepository.existsById(feedbackID)) {
            feedbackRepository.deleteById(feedbackID);
        } else {
            throw new RuntimeException("Feedback not found");
        }
    }
}
