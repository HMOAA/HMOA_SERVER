package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.Cart;
import lombok.Data;

import java.util.List;

@Data
public class NoteProductSelectRequestDto {

    private List<Long> productIds;

    public Cart toCartEntity(Long memberId, int totalPrice) {
        return Cart.builder()
                .memberId(memberId)
                .totalPrice(totalPrice)
                .productIds(productIds)
                .build();
    }
}
