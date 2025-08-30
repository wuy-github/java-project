package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUserId(Long userId);
    List<Feedback> findByWatchId(Long watchId);
    Optional<Feedback> findByUserAndWatch(User user, Watch watch);
}
