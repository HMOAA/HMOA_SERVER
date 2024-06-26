package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.recommend.survey.domain.Answer;
import hmoa.hmoaserver.recommend.survey.domain.AnswerNote;
import lombok.Data;

@Data
public class AnswerNoteSaveRequestDto {

    private String noteTitle;
    private Long answerId;
}
