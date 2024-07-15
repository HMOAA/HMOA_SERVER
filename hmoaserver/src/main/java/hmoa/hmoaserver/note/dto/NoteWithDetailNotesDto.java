package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;

import java.util.List;

@Data
public class NoteWithDetailNotesDto {

    private String noteName;
    private String notePhotoUrl;
    private List<DetailNoteResponseDto> detailNotes;

    public NoteWithDetailNotesDto(Note note, List<DetailNoteResponseDto> detailNotes) {
        this.noteName = note.getTitle();
        this.notePhotoUrl = note.getNotePhoto().getPhotoUrl();
        this.detailNotes = detailNotes;
    }
}
