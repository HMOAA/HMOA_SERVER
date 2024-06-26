package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.recommend.survey.domain.MemberAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {
}
