package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppraisalRepository extends JpaRepository<Appraisal, Long> {
    Optional<Appraisal> findByUserAndWatch(User user, Watch watch);
    List<Appraisal> findByUserId(long userId);
    List<Appraisal> findByWatchId(long watchId);
}
