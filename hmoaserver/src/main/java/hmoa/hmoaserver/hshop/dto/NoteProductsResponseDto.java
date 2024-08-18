package hmoa.hmoaserver.hshop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteProductsResponseDto {

    private int totalPrice;
    private List<NoteProductDetailResponseDto> noteProducts;

    public NoteProductsResponseDto(int totalPrice, List<NoteProductDetailResponseDto> noteProducts) {
        this.totalPrice = totalPrice;
        this.noteProducts = noteProducts;
    }
}
