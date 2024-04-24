package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PerfumeCommentLikedRepository extends JpaRepository<PerfumeCommentLiked, Long> {
    Optional<PerfumeCommentLiked> findByPerfumeCommentAndMember(PerfumeComment perfumeComment, Member member);
    Page<PerfumeCommentLiked> findAllByMemberOrderByIdDesc(Member member, Pageable pageable);

    @Query("SELECT pcl " +
            "FROM PerfumeCommentLiked pcl " +
            "WHERE pcl.member = ?1 AND pcl.id < ?2 " +
            "ORDER BY pcl.id Desc")
    Page<PerfumeCommentLiked> findAllByMemberNextCursor(Member member, Long cursor, Pageable pageable);
}
