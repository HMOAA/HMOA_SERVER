package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Answer;
import lombok.Data;

@Data
public class AnswerResponseDto {
    String content;

    public AnswerResponseDto(Answer answer) {
        this.content = answer.getContent();
    }
}
