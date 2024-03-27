package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerfumeCommentRepository extends JpaRepository<PerfumeComment, Long> {


    Page<PerfumeComment> findAllByMember(Member member, Pageable pageable);
    List<PerfumeComment> findAllByMemberId(Long memberId);
    Optional<PerfumeComment> findById(Long id);

    Page<PerfumeComment> findAllByPerfumeId(Long perfumeId, Pageable pageable);

    /**
     * 최신순
     */
    Page<PerfumeComment> findAllByPerfumeIdOrderByCreatedAtDescIdAsc(Long perfumeId, Pageable pageable);


    /**
     * 좋아요순
     */
    Page<PerfumeComment> findAllByPerfumeIdOrderByHeartCountDescIdAsc(Long perfumeId, Pageable pageable);
}
