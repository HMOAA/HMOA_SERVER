package hmoa.hmoaserver.hshop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATED("주문 생성"),
    PAY_WAITING("결제 대기"),
    PAY_CHECKING("결제 대기"),
    PAY_FAILED("결제 실패"),
    PAY_COMPLETE("결제 완료"),
    SHIPPING_WAITING("배송 준비 중"),
    SHIPPING_PROGRESS("배송 중"),
    SHIPPING_COMPLETE("배송 완료"),
    PURCHASE_CONFIRMATION("구매 확정");


    private final String description;
}
