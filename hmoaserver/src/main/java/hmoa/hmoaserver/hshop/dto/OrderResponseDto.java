package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.member.dto.MemberAddressResponseDto;
import hmoa.hmoaserver.member.dto.MemberInfoResponseDto;
import lombok.Data;

@Data
public class OrderResponseDto {

    private Long orderId;
    private OrderStatus orderStatus;
    private MemberInfoResponseDto memberInfo;
    private MemberAddressResponseDto memberAddress;
    private NoteProductsResponseDto productInfo;
    private int paymentAmount;
    private int shippingAmount;
    private int totalAmount;

    public OrderResponseDto(OrderEntity orderEntity, MemberInfoResponseDto memberInfo,
                            MemberAddressResponseDto memberAddressResponseDto, NoteProductsResponseDto noteProducts,
                            int shippingAmount) {
        this.orderId = orderEntity.getId();
        this.orderStatus = orderEntity.getStatus();
        this.memberInfo = memberInfo;
        this.memberAddress = memberAddressResponseDto;
        this.productInfo = noteProducts;
        this.paymentAmount = orderEntity.getTotalPrice();
        this.shippingAmount = shippingAmount;
        this.totalAmount = paymentAmount + shippingAmount;
    }
}
