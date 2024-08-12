package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Question;
import hmoa.hmoaserver.recommend.survey.domain.QuestionType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionResponseDto {
    private Long questionId;
    private String content;
    @Getter(AccessLevel.NONE)
    private boolean isMultipleChoice;
    private List<AnswerResponseDto> answers;

    public boolean getIsMultipleChoice() {
        return isMultipleChoice;
    }

    public QuestionResponseDto(Question question) {
        this.questionId = question.getId();
        this.content = question.getContent();
        this.isMultipleChoice = question.isMultiple();
        this.answers = question.getAnswers().stream().map(AnswerResponseDto::new).collect(Collectors.toList());
    }
}
