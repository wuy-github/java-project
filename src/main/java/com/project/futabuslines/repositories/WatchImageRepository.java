package com.project.futabuslines.repositories;

import com.project.futabuslines.models.WatchImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchImageRepository extends JpaRepository<WatchImage, Long> {
    List<WatchImage> findByWatchId(long id);

    Optional<WatchImage> findFirstByWatchId(Long id);
}
