package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class NoteSaveRequestDto {
    private String noteTitle;
    private String noteSubTitle;
    private String content;

    public Note toEntity() {
        return Note.builder()
                .title(noteTitle)
                .subTitle(noteSubTitle)
                .content(content)
                .build();
    }
}
