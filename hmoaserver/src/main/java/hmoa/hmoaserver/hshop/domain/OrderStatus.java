package hmoa.hmoaserver.hshop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATED("주문 생성"),
    PAY_WAITING("결제 대기"),
    PAY_CHECKING("결제 대기"),
    PAY_FAILED("결제 실패"),
    PAY_COMPLETE("결제 완료"),
    PAY_CANCEL("주문 취소"),
    SHIPPING_WAITING("배송 준비 중"),
    SHIPPING_PROGRESS("배송 중"),
    SHIPPING_COMPLETE("배송 완료"),
    PURCHASE_CONFIRMATION("구매 확정"),
    RETURN_COMPLETE("반품 완료"),
    RETURN_PROGRESS("반품 진행 중"),
    REVIEW_COMPLETE("리뷰 완료");

    private final String description;

    public static List<OrderStatus> getCancelStatus() {
        return List.of(PAY_CANCEL, RETURN_COMPLETE, RETURN_PROGRESS);
    }

    public static List<OrderStatus> getReviewStatus() {
        return List.of(PAY_CANCEL, RETURN_COMPLETE, RETURN_PROGRESS, REVIEW_COMPLETE);
    }
}
