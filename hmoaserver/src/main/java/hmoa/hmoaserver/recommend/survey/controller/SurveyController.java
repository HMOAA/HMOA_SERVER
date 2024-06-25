package hmoa.hmoaserver.recommend.survey.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.dto.SurveySaveRequestDto;
import hmoa.hmoaserver.recommend.survey.service.SurveyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"설문"})
@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> saveSurvey(@RequestBody SurveySaveRequestDto dto) {
        surveyService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
