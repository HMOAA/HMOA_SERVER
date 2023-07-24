package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.perfume.domain.Perfume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    /**
     * 한글 이름 , 영어 이름 포함하여 검색
     */
    @Query(
            value = "SELECT p FROM Perfume p WHERE p.koreanName LIKE %:koreanName% OR p.englishName LIKE %:englishName% ORDER BY p.koreanName ASC"
    )
    Page<Perfume> findAllSearch(@Param("koreanName") String koreanName, @Param("englishName") String englishName, Pageable pageable);

    Page<Perfume> findAllByBrandIdOrderByCreatedAtDesc(Long brandId, Pageable pageable);

    Page<Perfume> findAllByBrandIdOrderByHeartCountDesc(Long brandId, Pageable pageable);
}
