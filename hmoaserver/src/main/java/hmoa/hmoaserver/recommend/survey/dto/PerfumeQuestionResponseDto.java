package hmoa.hmoaserver.recommend.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@Builder
public class PerfumeQuestionResponseDto {

    private String content;
    private List<PerfumeAnswerResponseDto> answer;

    public PerfumeQuestionResponseDto(final String content, final List<PerfumeAnswerResponseDto> answer) {
        this.content = content;
        this.answer = answer;
    }
}
