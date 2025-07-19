package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Favorite;
import com.project.futabuslines.models.WatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchDetailRepository extends JpaRepository<WatchDetail, Long> {
    Optional<WatchDetail> findByWatchId(Long watchId);
}
