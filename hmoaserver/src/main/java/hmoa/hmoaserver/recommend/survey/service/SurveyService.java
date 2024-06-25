package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.dto.SurveySaveRequestDto;
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
    public Survey save(final SurveySaveRequestDto dto) {
        try {
            return surveyRepository.save(dto.toEntity());
        } catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
    
    public Survey findById(final Long surveyId) {
        return surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(null, Code.SURVEY_NOT_FOUND));
    }
}
