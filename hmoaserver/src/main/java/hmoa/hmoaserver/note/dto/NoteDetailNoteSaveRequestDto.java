package hmoa.hmoaserver.note.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoteDetailNoteSaveRequestDto {

    private List<Long> detailNotes;
}
