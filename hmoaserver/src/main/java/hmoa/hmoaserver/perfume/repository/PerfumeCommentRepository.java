package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PerfumeCommentRepository extends JpaRepository<PerfumeComment, Long> {


    Page<PerfumeComment> findAllByMemberId(Long memberId, Pageable pageable);
    List<PerfumeComment> findAllByMemberId(Long memberId);
    Optional<PerfumeComment> findById(Long id);

    Page<PerfumeComment> findAllByPerfumeId(Long perfumeId, Pageable pageable);

    /**
     * 최신순
     */
    Page<PerfumeComment> findAllByPerfumeIdOrderByCreatedAtDesc(Long perfumeId, Pageable pageable);

    /**
     * 좋아요순
     */
    Page<PerfumeComment> findAllByPerfumeIdOrderByHeartCountDesc(Long perfumeId, Pageable pageable);
}
