package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Answer;
import lombok.Data;

@Data
public class AnswerResponseDto {
    Long optionId;
    String option;

    public AnswerResponseDto(Answer answer) {
        this.optionId = answer.getId();
        this.option = answer.getContent();
    }
}
