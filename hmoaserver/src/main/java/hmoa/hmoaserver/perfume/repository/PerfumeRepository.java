package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.perfume.domain.Perfume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    /**
     * 한글 이름 , 영어 이름 포함하여 검색
     */
    @Query(
            value = "SELECT p" +
                    " FROM Perfume p" +
                    " WHERE CONCAT(p.brand.brandName, ' ', p.koreanName) LIKE %:koreanName% OR" +
                    " CONCAT(p.brand.englishName, ' ', p.englishName) LIKE %:englishName%" +
                    " ORDER BY p.koreanName ASC"
    )
    Page<Perfume> findAllSearch(@Param("koreanName") String koreanName, @Param("englishName") String englishName, Pageable pageable);

    Optional<Perfume> findByKoreanName(String koreanName);

    Page<Perfume> findAllByOrderByRelaseDateDescIdAsc(Pageable pageable);

    Page<Perfume> findAllByBrandIdOrderByCreatedAtDescIdAsc(Long brandId, Pageable pageable);

    Page<Perfume> findAllByBrandIdOrderByKoreanNameAscIdAsc(Long brandId, Pageable pageable);

    Page<Perfume> findAllByBrandIdOrderByHeartCountDescIdAsc(Long brandId, Pageable pageable);
    Page<Perfume> findAllByBrandIdAndIdNot(Long brandId, Long excludedId, Pageable pageable);

    Optional<Perfume> findByBrandIdAndKoreanName(Long brandId, String koreanName);
}
