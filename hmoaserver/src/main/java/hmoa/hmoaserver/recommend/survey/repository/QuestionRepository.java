package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.recommend.survey.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
