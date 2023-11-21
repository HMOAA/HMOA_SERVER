package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoteDetailResponseDto {
    private Long noteId;
    private String noteTitle;
    private String noteSubtitle;
    private String content;

    public NoteDetailResponseDto(Note note) {
        this.noteId = note.getId();
        this.noteTitle = note.getTitle();
        this.noteSubtitle = note.getSubtitle();
        this.content = note.getContent();
    }
}
