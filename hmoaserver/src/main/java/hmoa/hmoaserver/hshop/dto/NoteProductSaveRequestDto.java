package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.note.domain.Note;
import lombok.Data;

@Data
public class NoteProductSaveRequestDto {

    private String noteName;
    private int price;

    public NoteProduct toEntity(Note note) {
        return NoteProduct.builder()
                .note(note)
                .price(price)
                .build();
    }
}
