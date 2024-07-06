package hmoa.hmoaserver.recommend.survey.dto;

import lombok.Data;

@Data
public class AnswerNoteSaveRequestDto {

    private String noteTitle;
    private Long answerId;
}
