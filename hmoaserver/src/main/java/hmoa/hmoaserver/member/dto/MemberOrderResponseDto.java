package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.dto.OrderInfoResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberOrderResponseDto {

    private Long orderId;
    private OrderStatus orderStatus;
    private OrderInfoResponseDto orderProducts;
    private String courierCompany;
    private String trackingNumber;

    public MemberOrderResponseDto(OrderEntity order, OrderInfoResponseDto orderProducts) {
        this.orderId = order.getId();
        this.orderStatus = order.getStatus();
        this.orderProducts = orderProducts;
        this.courierCompany = order.getCourierCompany();
        this.trackingNumber = order.getTrackingNumber();
    }
}
