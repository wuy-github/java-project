    package com.project.futabuslines.repositories;

    import com.project.futabuslines.models.Watch;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface WatchRepository extends JpaRepository<Watch, Long> {
        Page<Watch> findAll(Pageable pageable);
        List<Watch> findByBrandId(long brandId);
        List<Watch> findByCategoryId(long categoryId);

    }
