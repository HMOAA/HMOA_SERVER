package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.recommend.survey.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
