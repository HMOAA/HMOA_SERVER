package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSelectResponseDto {

    private Long orderId;
    private String orderInfo;

    public OrderSelectResponseDto(OrderEntity order) {
        this.orderId = order.getId();
        this.orderInfo = order.getTitle() + " " + DateUtils.extractOrderSelectDate(order.getCreatedAt());
    }
}
