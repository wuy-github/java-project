package com.project.futabuslines.services;

import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {
    private final UserRepository userRepository;
    private final WatchRepository watchRepository;
    private final OrderRepository orderRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final AppraisalRepository appraisalRepository;
    private final CartRepository cartRepository;
    private final FeedbackRepository feedbackRepository;

    public User findUserById(long userId) throws DataNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id = " + userId));
    }
    public Watch findWatchById(long watchId) throws DataNotFoundException {
        return watchRepository.findById(watchId)
                .orElseThrow(()-> new DataNotFoundException("Cannot find watch with id = " + watchId));
    }
    public Order findOrderById(long orderId) throws DataNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new DataNotFoundException("Cannot find order with id: "+ orderId));
    }
    public Brand findBrandById(long brandId) throws DataNotFoundException {
        return brandRepository.findById(brandId)
                .orElseThrow(()-> new DataNotFoundException("Cannot found brand with id = " + brandId));
    }
    public Category findCategoryById(long categoryId) throws DataNotFoundException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new DataNotFoundException("Cannot found category with id = " + categoryId));
    }
    public Appraisal findAppraisalById(long appraisalId) throws DataNotFoundException {
        return appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found appraisal with id = " + appraisalId));
    }
    public Cart findCartById(long cartId) throws DataNotFoundException {
        return cartRepository.findById(cartId)
                .orElseThrow(()-> new DataNotFoundException("Cannot found cart with id = " + cartId));
    }

    public Feedback findFeedbackById(long feedBackId) throws DataNotFoundException {
        return feedbackRepository.findById(feedBackId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found feedback with id = " + feedBackId));
    }
}
