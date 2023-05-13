package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfumeCommentHeartRepository extends JpaRepository<PerfumeCommentHeart, Long> {
    Optional<PerfumeCommentHeart> findByPerfumeCommentAndMember(PerfumeComment perfumeComment, Member member);

    List<PerfumeCommentHeart> findAllByMemberId(Long memberId);

}
