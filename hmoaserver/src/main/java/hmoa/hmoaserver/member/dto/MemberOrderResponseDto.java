package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.common.DateUtils;
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
    private String createdAt;
    private String courierCompany;
    private String trackingNumber;

    public MemberOrderResponseDto(OrderEntity order, OrderInfoResponseDto orderProducts) {
        this.orderId = order.getId();
        this.orderStatus = order.getStatus();
        this.orderProducts = orderProducts;
        this.createdAt = DateUtils.extractOrderDate(order.getCreatedAt());
        this.courierCompany = order.getCourierCompany();
        this.trackingNumber = order.getTrackingNumber();
    }
}
