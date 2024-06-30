package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.recommend.survey.domain.NoteRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRecommendRepository extends JpaRepository<NoteRecommend, Long> {
}
