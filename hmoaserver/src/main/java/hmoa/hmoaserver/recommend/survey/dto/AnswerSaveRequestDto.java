package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Answer;
import hmoa.hmoaserver.recommend.survey.domain.Question;
import lombok.Data;

@Data
public class AnswerSaveRequestDto {
    private String content;

    public Answer toEntity(Question question) {
        return Answer.builder()
                .content(content)
                .question(question)
                .build();
    }
}
