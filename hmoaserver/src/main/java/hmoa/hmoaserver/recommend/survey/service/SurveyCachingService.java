package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.config.setting.CacheName;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.recommend.survey.controller.constant.SurveyConstant;
import hmoa.hmoaserver.recommend.survey.domain.Question;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.domain.SurveyType;
import hmoa.hmoaserver.recommend.survey.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyCachingService {
    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Cacheable(cacheNames = CacheName.NOTE_SURVEY, key = "'NOTE_SURVEY'")
    public SurveyResponseDto getNotesSurvey(Survey survey) {
        return new SurveyResponseDto(survey);
    }

    @Cacheable(cacheNames = CacheName.QUESTION, key = "#questionId")
    public Question getQuestionById(Long questionId) {
        return questionService.getQuestion(questionId);
    }

    @Cacheable(cacheNames = CacheName.SURVEY, key = "#surveyType")
    public Survey getSurvey(SurveyType surveyType) {
        return surveyService.findBySurveyType(surveyType);
    }

    @Cacheable(cacheNames = CacheName.PERFUME_SURVEY, key = "'PERFUME_SURVEY'")
    public PerfumeSurveyResponseDto getPerfumeSurvey(final List<Note> notes, final Question question) {
        List<PerfumeAnswerResponseDto> answers = notes.stream()
                .map(PerfumeAnswerResponseDto::new)
                .collect(Collectors.toList());

        PerfumeQuestionResponseDto perfumeQuestionResponseDto = new PerfumeQuestionResponseDto(SurveyConstant.PERFUME_QUESTION, answers);
        QuestionResponseDto questionResponseDto = new QuestionResponseDto(question);

        return new PerfumeSurveyResponseDto(questionResponseDto, perfumeQuestionResponseDto);
    }
}
