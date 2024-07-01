package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.domain.SurveyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findBySurveyType(SurveyType surveyType);
}
