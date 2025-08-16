package com.project.futabuslines.services;

import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Order;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.repositories.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {
    private final UserRepository userRepository;
    private final WatchRepository watchRepository;
    private final OrderRepository orderRepository;

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
}
