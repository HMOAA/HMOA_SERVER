package hmoa.hmoaserver.brand.repository;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.domain.BrandLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandLikedMemberRepository extends JpaRepository<BrandLikedMember, Long> {

    List<Long> findBrandLikedMemberById(@Param("memberId") Long memberId);

    List<BrandLikedMember> findAllByMemberId(Long memberId);

    Optional<BrandLikedMember> findByMemberAndBrand(Member member, Brand brand);
}
