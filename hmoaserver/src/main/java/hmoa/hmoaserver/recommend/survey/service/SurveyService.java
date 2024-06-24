package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @Transactional
    public Survey save(Survey survey) {
        try {
            return surveyRepository.save(survey);
        } catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
