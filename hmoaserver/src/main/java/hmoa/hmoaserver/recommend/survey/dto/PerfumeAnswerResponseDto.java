package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.domain.NoteDetailNote;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PerfumeAnswerResponseDto {

    private String category;
    private List<String> notes;

    public PerfumeAnswerResponseDto(final Note note) {
        this.category = note.getTitle();
        this.notes = getDetailNotes(note.getNoteDetailNotes());
    }

    private static List<String> getDetailNotes(final List<NoteDetailNote> noteDetailNotes) {
        List<String> notes = new ArrayList<>();
        noteDetailNotes.forEach(noteDetailNote -> {
            notes.add(noteDetailNote.getDetailNote().getTitle());
        });
        return notes;
    }
}
