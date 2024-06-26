package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Question;
import hmoa.hmoaserver.recommend.survey.domain.Survey;
import lombok.Data;

@Data
public class QuestionSaveRequestDto {
    private String content;

    public Question toEntity(Survey survey) {
        return Question.builder()
                .content(content)
                .survey(survey)
                .build();
    }
}
