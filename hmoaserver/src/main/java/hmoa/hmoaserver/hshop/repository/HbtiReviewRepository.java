package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.HbtiReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HbtiReviewRepository extends JpaRepository<HbtiReview, Long> {

    Page<HbtiReview> findAllByOrderByHeartCountDescCreatedAtDesc(Pageable pageable);

    @Query("SELECT hr " +
            "FROM HbtiReview hr " +
            "WHERE hr.memberId = ?1 AND " +
            "hr.id < ?2 " +
            "ORDER BY hr.createdAt DESC, hr.id Desc")
    Page<HbtiReview> findAllByMemberIdOrderByCreatedAtDesc(Long memberId, Long cursor, Pageable pageable);

    Optional<HbtiReview> findByOrderIdAndMemberId(Long orderId, Long memberId);
}