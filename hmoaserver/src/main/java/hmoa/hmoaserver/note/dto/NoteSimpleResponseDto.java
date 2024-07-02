package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;

@Data
public class NoteSimpleResponseDto {
    private String noteName;
    private String content;

    public NoteSimpleResponseDto(Note note) {
        this.noteName = note.getTitle();
        this.content = note.getContent();
    }
}
