package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.hshop.domain.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatusUpdateRequestDto {

    private Long orderId;
    private OrderStatus status;
}
