package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.perfume.domain.PerfumeLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerfumeLikedMemberRepository extends JpaRepository<PerfumeLikedMember, Long> {

    List<Long> findPerfumeLikedMemberById(@Param("memberId") Long memberId);

    void deleteByMemberIdAndPerfumeId(Long memberId, Long perfumeId);

    void deleteAllByPerfumeId(Long perfumeId);

    boolean existsByMemberIdAndPerfumeId(Long memberId, Long perfumeId);
}
