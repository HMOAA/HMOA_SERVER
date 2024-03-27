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
    Page<PerfumeComment> findAllByPerfumeIdOrderByCreatedAtDescIdDesc(Long perfumeId, Pageable pageable);

    @Query("SELECT pc " +
            "FROM PerfumeComment pc " +
            "WHERE pc.perfume.id = ?1 " +
            "AND pc.id < ?2 " +
            "ORDER BY pc.createdAt DESC, pc.id DESC")
    Page<PerfumeComment> findPerfumeCommentOrderByCreatedAtNextCursor(Long perfumeId, Long cursor, Pageable pageable);
    Long countByPerfumeId(Long perfumeId);
    /**
     * 좋아요순
     */
    Page<PerfumeComment> findAllByPerfumeIdOrderByHeartCountDescIdAsc(Long perfumeId, Pageable pageable);
}
