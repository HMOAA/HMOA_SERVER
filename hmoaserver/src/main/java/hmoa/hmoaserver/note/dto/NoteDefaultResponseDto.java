package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoteDefaultResponseDto {
    private Long noteId;
    private String noteTitle;
    private String noteSubtitle;

    public NoteDefaultResponseDto(Note note) {
        this.noteId = note.getId();
        this.noteTitle = note.getTitle();
        this.noteSubtitle = note.getSubtitle();
    }
}
