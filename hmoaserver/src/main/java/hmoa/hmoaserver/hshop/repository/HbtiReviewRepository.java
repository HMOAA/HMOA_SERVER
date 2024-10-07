package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.HbtiReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HbtiReviewRepository extends JpaRepository<HbtiReview, Long> {

    Page<HbtiReview> findAllByOrderByHeartCountDescCreatedAtDesc(Pageable pageable);
}
