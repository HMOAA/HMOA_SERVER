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
    private int notesCount;
    private List<DetailNoteResponseDto> notes;
    private int price;

    public NoteProductDetailResponseDto(long productId, NoteWithDetailNotesDto dto, int price) {
        this.productId = productId;
        this.productName = dto.getNoteName();
        this.productPhotoUrl = dto.getNotePhotoUrl();
        this.notes = dto.getDetailNotes();
        this.notesCount = notes.size();
        this.price = price;
    }
}
