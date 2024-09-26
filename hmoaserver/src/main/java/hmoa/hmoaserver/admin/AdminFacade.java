package hmoa.hmoaserver.admin;

import hmoa.hmoaserver.admin.dto.OrderDeliverySaveRequestDto;
import hmoa.hmoaserver.admin.dto.TrackingCallbackRequestDto;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminFacade {

    private final OrderService orderService;

    // 운송장 등록
    public void saveDeliveryInfo(OrderDeliverySaveRequestDto dto) {
        OrderEntity order = orderService.findById(dto.getOrderId());
        orderService.updateOrderStatus(order, OrderStatus.SHIPPING_PROGRESS);
        orderService.updateDeliveryInfo(order, dto.getCourierCountry(), dto.getTrackingNumber());
    }

    // 배송 상태 변화 받을 시 처리하는 로직
    public void checkTracking(TrackingCallbackRequestDto dto) {
        OrderEntity order = orderService.getByTrackingNumber(dto.getTrackingNumber());
        log.info("check Tracking");
    }
}
