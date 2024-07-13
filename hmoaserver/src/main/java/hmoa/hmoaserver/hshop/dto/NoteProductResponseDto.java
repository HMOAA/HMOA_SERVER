package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.note.domain.NoteDetailNote;
import lombok.Data;

import java.util.List;

@Data
public class NoteProductResponseDto {

    private Long productId;
    private String productName;
    private String productDetails;
    private Boolean isRecommended;

    public NoteProductResponseDto(NoteProduct noteProduct, boolean isRecommended) {
        this.productId = noteProduct.getId();
        this.productName = noteProduct.getNote().getTitle();
        this.productDetails = getDetails(noteProduct.getNote().getNoteDetailNotes());
        this.isRecommended = isRecommended;
    }

    private static String getDetails(List<NoteDetailNote> noteDetailNotes) {
        String title = "";

        for (NoteDetailNote noteDetailNote : noteDetailNotes) {
            title += noteDetailNote.getDetailNote().getTitle() + ", ";
        }

        return title.substring(0, title.length() - 2);
    }
}
