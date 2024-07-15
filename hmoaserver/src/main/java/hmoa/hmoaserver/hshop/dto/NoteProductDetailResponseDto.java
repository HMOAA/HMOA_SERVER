package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.note.dto.DetailNoteResponseDto;
import hmoa.hmoaserver.note.dto.NoteWithDetailNotesDto;
import lombok.Data;

import java.util.List;

@Data
public class NoteProductDetailResponseDto {
    private long productId;
    private String productName;
    private String productPhotoUrl;
    private List<DetailNoteResponseDto> notes;

    public NoteProductDetailResponseDto(long productId, NoteWithDetailNotesDto dto) {
        this.productId = productId;
        this.productName = dto.getNoteName();
        this.productPhotoUrl = dto.getNotePhotoUrl();
        this.notes = dto.getDetailNotes();
    }
}
