package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.DetailNote;
import lombok.Data;

@Data
public class DetailNoteResponseDto {

    private String noteName;
    private String noteContent;

    public DetailNoteResponseDto(DetailNote detailNote) {
        this.noteName = detailNote.getTitle();
        this.noteContent = detailNote.getContent();
    }
}
