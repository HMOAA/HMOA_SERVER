package hmoa.hmoaserver.brand.repository;

import hmoa.hmoaserver.brand.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandName(String brandName);
    Optional<Brand> findById(Long id);

    /**
     * 한글 이름 , 영어 이름 포함하여 검색
     */
    @Query(
            value = "SELECT p FROM Brand p WHERE p.brandName LIKE %:brandName% OR p.englishName LIKE %:englishName% ORDER BY p.brandName ASC"
    )
    Page<Brand> findAllSearch(@Param("brandName") String brandName, @Param("englishName") String englishName, Pageable pageable);

    /**
     * 페이징 x
     */
    @Query(
            value = "SELECT p FROM Brand p WHERE p.brandName LIKE %:brandName% OR p.englishName LIKE %:englishName% ORDER BY p.brandName ASC"
    )
    List<Brand> findAllSearch(@Param("brandName") String brandName, @Param("englishName") String englishName);
}
