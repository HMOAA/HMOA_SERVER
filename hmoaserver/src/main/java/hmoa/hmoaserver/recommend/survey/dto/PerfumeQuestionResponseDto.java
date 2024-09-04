package hmoa.hmoaserver.recommend.survey.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeQuestionResponseDto {

    private String content;
    private List<PerfumeAnswerResponseDto> answer;
    @Getter(AccessLevel.NONE)
    private boolean isMultipleChoice;

    public boolean getIsMultipleChoice() {
        return isMultipleChoice;
    }

    public PerfumeQuestionResponseDto(final String content, final List<PerfumeAnswerResponseDto> answer) {
        this.content = content;
        this.answer = answer;
        this.isMultipleChoice = true;
    }
}
