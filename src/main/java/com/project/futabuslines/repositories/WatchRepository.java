package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Long> {
}
