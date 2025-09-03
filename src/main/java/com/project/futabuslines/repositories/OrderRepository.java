package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Order;
import com.project.futabuslines.models.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    Page<Order> findAll(Pageable pageable);
}
