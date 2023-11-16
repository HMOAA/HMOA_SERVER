package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;

@Data
public class NoteSaveRequestDto {
    private String noteTitle;
    private String noteSubtitle;
    private String content;

    public Note toEntity() {
        return Note.builder()
                .title(noteTitle)
                .subtitle(noteSubtitle)
                .content(content)
                .build();
    }
}
