package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.member.dto.MemberAddressResponseDto;
import hmoa.hmoaserver.member.dto.MemberInfoResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDeliveryListResponseDto {

    private Long orderId;
    private OrderStatus orderStatus;
    private MemberInfoResponseDto orderMemberInfo;
    private MemberAddressResponseDto address;

    public OrderDeliveryListResponseDto(OrderEntity order, MemberAddressResponseDto address, MemberInfoResponseDto orderMemberInfo) {
        this.orderId = order.getId();
        this.orderStatus = order.getStatus();
        this.orderMemberInfo = orderMemberInfo;
        this.address = address;
    }
}
