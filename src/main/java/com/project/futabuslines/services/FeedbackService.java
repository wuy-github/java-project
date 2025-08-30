package com.project.futabuslines.services;

import com.project.futabuslines.dtos.FeedbackDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.FeedbackRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.responses.AppraisalResponse;
import com.project.futabuslines.responses.FeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    private final EntityFinder entityFinder;

    public FeedbackResponse createFeedback(Long userId, FeedbackDTO feedbackDTO) throws Exception {
        User user = entityFinder.findUserById(userId);
        Watch watch = entityFinder.findWatchById(feedbackDTO.getWatchId());
        // Chỉ cho phép mỗi user có tối đa 5 feedback
//        int count = feedbackRepository.findByUserId(userId).size();
//        if (count >= 5) {
//            throw new InvalidParamException("Number of feedbacks must be <= 5");
//        }
        Optional<Feedback> existingFeedback = feedbackRepository.findByUserAndWatch(user, watch);

        if(existingFeedback.isPresent()){
            throw new ResourceAlreadyExistsException("Da danh gia san pham nay roi !!!");
        }

        Feedback newFeedback = Feedback.builder()
                .user(user)
                .watch(watch)
                .rating(feedbackDTO.getRating())
                .description(feedbackDTO.getDescription())
                .imageUrl(feedbackDTO.getImageUrls())
                .build();

        feedbackRepository.save(newFeedback);
        return FeedbackResponse.fromFeedback(newFeedback);
    }

    public List<FeedbackResponse> getFeedbacksByUserId(Long userId) throws Exception {
        entityFinder.findUserById(userId);

        List<Feedback> feedbackList = feedbackRepository.findByUserId(userId);

        return feedbackList.stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());
    }

    public FeedbackResponse getFeedbackDetailById(Long feedbackId) throws Exception {
        Feedback feedback = entityFinder.findFeedbackById(feedbackId);
        return FeedbackResponse.fromFeedback(feedback);
    }

    public List<FeedbackResponse> getFeedbackByWatchId(Long watchId) throws Exception {
        entityFinder.findWatchById(watchId);
        List<Feedback> feedbacks = feedbackRepository.findByWatchId(watchId);
        return feedbacks.stream()
                .map((FeedbackResponse::fromFeedback))
                .collect(Collectors.toList());
    }

    public List<FeedbackResponse> getAllFeedBack() throws Exception{
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream()
                .map((FeedbackResponse::fromFeedback))
                .collect(Collectors.toList());
    }

    public FeedbackResponse updateFeedback(Long feedbackId, FeedbackDTO feedbackDTO) throws DataNotFoundException {
        entityFinder.findWatchById(feedbackDTO.getWatchId());
        Feedback feedback = entityFinder.findFeedbackById(feedbackId);

        feedback.setDescription(feedbackDTO.getDescription());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setImageUrl(feedbackDTO.getImageUrls());

        feedbackRepository.save(feedback);
        return FeedbackResponse.fromFeedback(feedback);

    }

    public void deleteFeedback(Long id) throws DataNotFoundException {
        entityFinder.findFeedbackById(id);
        feedbackRepository.deleteById(id);
    }
}


