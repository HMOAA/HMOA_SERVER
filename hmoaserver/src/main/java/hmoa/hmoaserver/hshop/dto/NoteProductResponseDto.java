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
    private String productPhotoUrl;
    private Boolean isRecommended;
    private int price;

    public NoteProductResponseDto(NoteProduct noteProduct, boolean isRecommended) {
        this.productId = noteProduct.getId();
        this.productName = noteProduct.getNote().getTitle();
        this.productDetails = getDetails(noteProduct.getNote().getNoteDetailNotes());
        this.productPhotoUrl = noteProduct.getNote().getNotePhoto().getPhotoUrl();
        this.isRecommended = isRecommended;
        this.price = noteProduct.getPrice();
    }

    private static String getDetails(List<NoteDetailNote> noteDetailNotes) {
        String title = "";

        for (NoteDetailNote noteDetailNote : noteDetailNotes) {
            title += noteDetailNote.getDetailNote().getTitle() + ", ";
        }

        return title.substring(0, title.length() - 2);
    }
}
