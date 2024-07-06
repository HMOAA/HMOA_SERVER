package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Survey;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SurveyResponseDto {
    private String title;
    private List<QuestionResponseDto> questions;

    public SurveyResponseDto(Survey survey) {
        this.title = survey.getTitle();
        this.questions = survey.getQuestions().stream().map(QuestionResponseDto::new).collect(Collectors.toList());
    }
}
