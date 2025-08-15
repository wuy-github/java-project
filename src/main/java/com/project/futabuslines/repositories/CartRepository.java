package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndWatchId(Long id, Long id1);

    List<Cart> findByUserId(long userId);
}
