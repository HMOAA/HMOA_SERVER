package hmoa.hmoaserver.recommend.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PerfumeSurveyResponseDto {

    private QuestionResponseDto priceQuestion;
    private PerfumeQuestionResponseDto noteQuestion;

    public PerfumeSurveyResponseDto(QuestionResponseDto priceQuestion, PerfumeQuestionResponseDto noteQuestion) {
        this.priceQuestion = priceQuestion;
        this.noteQuestion = noteQuestion;
    }
}
