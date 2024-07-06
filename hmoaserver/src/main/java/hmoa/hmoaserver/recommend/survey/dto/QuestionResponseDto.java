package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Question;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionResponseDto {
    private Long questionId;
    private String content;
    private List<AnswerResponseDto> answers;

    public QuestionResponseDto(Question question) {
        this.questionId = question.getId();
        this.content = question.getContent();
        this.answers = question.getAnswers().stream().map(AnswerResponseDto::new).collect(Collectors.toList());
    }
}
