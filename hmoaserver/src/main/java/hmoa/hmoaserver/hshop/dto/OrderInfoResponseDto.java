package hmoa.hmoaserver.hshop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderInfoResponseDto {

    private NoteProductsResponseDto productInfo;
    private int paymentAmount;
    private int shippingAmount;
    private int totalAmount;

    public OrderInfoResponseDto(NoteProductsResponseDto productInfo, int paymentAmount, int shippingAmount) {
        this.productInfo = productInfo;
        this.paymentAmount = paymentAmount;
        this.shippingAmount = shippingAmount;
        this.totalAmount = paymentAmount + shippingAmount;
    }
}
