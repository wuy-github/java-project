package com.project.futabuslines.services;

import com.project.futabuslines.dtos.FeedbackDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.FeedbackRepository;
import com.project.futabuslines.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    private final EntityFinder entityFinder;

    public Feedback createFeedback(Long userId, FeedbackDTO feedbackDTO) throws Exception {
        User user = entityFinder.findUserById(feedbackDTO.getUserId());
        Watch watch = entityFinder.findWatchById(feedbackDTO.getWatchId());
        // Chỉ cho phép mỗi user có tối đa 5 feedback
//        int count = feedbackRepository.findByUserId(userId).size();
//        if (count >= 5) {
//            throw new InvalidParamException("Number of feedbacks must be <= 5");
//        }

        Feedback newFeedback = Feedback.builder()
                .user(user)
                .watch(watch)
                .rating(feedbackDTO.getRating())
                .description(feedbackDTO.getDescription())
                .imageUrl(feedbackDTO.getImageUrls())
                .build();

        return feedbackRepository.save(newFeedback);
    }

    public List<Feedback> getFeedbacksByUserId(Long userId) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user với id: " + userId));

        List<Feedback> feedbackList = feedbackRepository.findByUserId(userId);

        return feedbackList;
    }

    public Feedback getFeedbackDetailById(Long feedbackId) throws Exception {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy feedback với id: " + feedbackId));
    }

}


