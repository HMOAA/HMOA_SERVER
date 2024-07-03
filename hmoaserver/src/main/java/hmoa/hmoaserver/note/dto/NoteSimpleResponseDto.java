package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;

@Data
public class NoteSimpleResponseDto {
    private Long noteId;
    private String noteName;
    private String content;
    private String notePhotoUrl;

    public NoteSimpleResponseDto(Note note) {
        this.noteId = note.getId();
        this.noteName = note.getTitle();
        this.content = note.getContent();
        this.notePhotoUrl = note.getNotePhoto().getPhotoUrl();
    }
}
