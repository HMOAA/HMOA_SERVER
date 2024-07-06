package hmoa.hmoaserver.note.dto;

import hmoa.hmoaserver.note.domain.DetailNote;
import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;

@Data
public class DetailNoteSaveRequestDto {

    private String title;
    private String content;

    public DetailNote toEntity() {
        return DetailNote.builder()
                .title(title)
                .content(content)
                .build();
    }
}
