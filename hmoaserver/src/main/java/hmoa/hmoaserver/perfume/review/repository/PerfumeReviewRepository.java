package hmoa.hmoaserver.perfume.review.repository;

import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeReviewRepository extends JpaRepository<PerfumeReview, Long> {
}
