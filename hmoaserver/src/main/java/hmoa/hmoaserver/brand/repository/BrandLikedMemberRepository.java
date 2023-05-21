package hmoa.hmoaserver.brand.repository;

import hmoa.hmoaserver.brand.domain.BrandLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandLikedMemberRepository extends JpaRepository<BrandLikedMember, Long> {

    List<Long> findBrandLikedMemberById(@Param("memberId") Long memberId);

    void deleteByMemberIdAndBrandId(Long memberId, Long brandId);

    void deleteAllByBrandId(Long brandId);

    boolean existsByMemberIdAndBrandId(Long memberId, Long brandId);
}
