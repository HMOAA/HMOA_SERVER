package hmoa.hmoaserver.recommend.survey.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.recommend.survey.domain.Answer;
import hmoa.hmoaserver.recommend.survey.domain.Question;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.dto.AnswerSaveRequestDto;
import hmoa.hmoaserver.recommend.survey.dto.QuestionSaveRequestDto;
import hmoa.hmoaserver.recommend.survey.dto.SurveySaveRequestDto;
import hmoa.hmoaserver.recommend.survey.service.AnswerService;
import hmoa.hmoaserver.recommend.survey.service.QuestionService;
import hmoa.hmoaserver.recommend.survey.service.SurveyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"설문"})
@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> saveSurvey(@RequestBody SurveySaveRequestDto dto) {
        surveyService.save(dto);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @PostMapping("/save-question/{surveyId}")
    public ResponseEntity<ResultDto<Object>> saveQuestion(@PathVariable Long surveyId, @RequestBody QuestionSaveRequestDto dto) {
        Survey survey = surveyService.findById(surveyId);
        questionService.save(dto, survey);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @PostMapping("/save-answer/{questionId}")
    public ResponseEntity<ResultDto<Object>> saveAnswer(@PathVariable Long questionId, @RequestBody AnswerSaveRequestDto dto) {
        Question question = questionService.findById(questionId);
        answerService.save(dto, question);

        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
