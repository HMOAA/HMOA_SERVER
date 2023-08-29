package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PerfumeLikedMemberRepository extends JpaRepository<PerfumeLikedMember, Long> {

    @Query(value = "SELECT perfume_id " +
            "FROM PerfumeLikedMember plm " +
            "WHERE plm.member_id = :memberId " +
            "ORDER BY plm.created_at DESC", nativeQuery = true)
    List<Long> findPerfumeLikedMemberById(@Param("memberId") Long memberId);

    void deleteByMemberIdAndPerfumeId(Long memberId, Long perfumeId);

    void deleteAllByPerfumeId(Long perfumeId);

    Optional<PerfumeLikedMember> findByMemberAndPerfume(Member member, Perfume perfume);
}
