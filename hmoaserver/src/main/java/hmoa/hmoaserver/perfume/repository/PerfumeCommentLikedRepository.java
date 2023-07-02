package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfumeCommentLikedRepository extends JpaRepository<PerfumeCommentLiked, Long> {
    Optional<PerfumeCommentLiked> findByPerfumeCommentAndMember(PerfumeComment perfumeComment, Member member);
    List<PerfumeCommentLiked> findAllByMemberId(Long memberId);
}
