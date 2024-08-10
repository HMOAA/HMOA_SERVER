package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderResponseDto {

    private Long orderId;
    private OrderStatus orderStatus;
    private boolean isExistMemberInfo;
    private boolean isExistMemberAddress;

    public OrderResponseDto(OrderEntity orderEntity, boolean isExistMemberInfo, boolean isExistMemberAddress) {
        this.orderId = orderEntity.getId();
        this.orderStatus = orderEntity.getStatus();
        this.isExistMemberInfo = isExistMemberInfo;
        this.isExistMemberAddress = isExistMemberAddress;
    }
}
