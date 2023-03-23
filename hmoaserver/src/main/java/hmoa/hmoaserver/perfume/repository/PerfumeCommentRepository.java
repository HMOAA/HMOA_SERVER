package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeCommentRepository extends JpaRepository<PerfumeComment, Long> {
}
