package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.recommend.survey.controller.constant.SurveyConstant;
import hmoa.hmoaserver.recommend.survey.domain.Question;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.domain.SurveyType;
import hmoa.hmoaserver.recommend.survey.dto.*;
import hmoa.hmoaserver.recommend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public Survey findBySurveyType(final SurveyType surveyType) {
        return surveyRepository.findBySurveyType(surveyType).orElseThrow(() -> new CustomException(null, Code.SURVEY_NOT_FOUND));
    }

    @Cacheable(value = "perfumeSurveyCache", key = "'perfumeSurveyDto'")
    public PerfumeSurveyResponseDto getPerfumeSurvey(final List<Note> notes, final Question question) {
        List<PerfumeAnswerResponseDto> answers = notes.stream()
                .map(PerfumeAnswerResponseDto::new)
                .collect(Collectors.toList());

        PerfumeQuestionResponseDto perfumeQuestionResponseDto = new PerfumeQuestionResponseDto(SurveyConstant.PERFUME_QUESTION, answers);
        QuestionResponseDto questionResponseDto = new QuestionResponseDto(question);

        return new PerfumeSurveyResponseDto(questionResponseDto, perfumeQuestionResponseDto);
    }
}
