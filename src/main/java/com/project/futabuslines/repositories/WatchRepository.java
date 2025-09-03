    package com.project.futabuslines.repositories;

    import com.project.futabuslines.models.Watch;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface WatchRepository extends JpaRepository<Watch, Long> {
        Page<Watch> findAll(Pageable pageable);
        List<Watch> findByBrandId(long brandId);
        List<Watch> findByCategoryId(long categoryId);
        @Query("""
        SELECT w FROM Watch w
        JOIN w.brand b
        JOIN w.category c
        WHERE (:brandName IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :brandName, '%')))
          AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%')))
          AND (:excludedStatus IS NULL OR LOWER(w.status) <> LOWER(:excludedStatus))
    """)
        Page<Watch> findByBrandAndCategoryName(
                @Param("brandName") String brandName,
                @Param("categoryName") String categoryName,
                @Param("excludedStatus") String excludedStatus,
                Pageable pageable
        );
        List<Watch> findAllByStatusNot(String status);

        // Phân trang nhưng loại trừ SOLD_OUT (dùng cho Page)
        Page<Watch> findAllByStatusNot(String status, Pageable pageable);

    }
