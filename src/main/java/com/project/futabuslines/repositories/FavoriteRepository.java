package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Favorite;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserAndWatch(User user, Watch watch);
    List<Favorite> findByUserIdAndIsActiveTrue(Long userId);
}
