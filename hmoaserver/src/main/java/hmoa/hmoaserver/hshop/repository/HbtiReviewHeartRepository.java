package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.HbtiReviewHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HbtiReviewHeartRepository extends JpaRepository<HbtiReviewHeart, Long> {

    Optional<HbtiReviewHeart> findByHbtiReviewIdAndMemberId(Long hbtiReviewId, Long memberId);
    List<HbtiReviewHeart> findAllByHbtiReviewId(Long hbtiReviewId);
}
