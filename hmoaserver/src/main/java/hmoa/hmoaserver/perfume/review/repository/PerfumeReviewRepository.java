package hmoa.hmoaserver.perfume.review.repository;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeReviewRepository extends JpaRepository<PerfumeReview, Long> {
    Optional<PerfumeReview> findByPerfume(Perfume perfume);
    Optional<PerfumeReview> findByPerfumeId(Long perfumeId);
}
