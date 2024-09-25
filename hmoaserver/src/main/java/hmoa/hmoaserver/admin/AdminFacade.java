package hmoa.hmoaserver.admin;

import hmoa.hmoaserver.admin.dto.OrderDeliverySaveRequestDto;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFacade {

    private final OrderService orderService;

    public void saveDeliveryInfo(OrderDeliverySaveRequestDto dto) {
        OrderEntity order = orderService.findById(dto.getOrderId());
        orderService.updateOrderStatus(order, OrderStatus.SHIPPING_PROGRESS);
        orderService.updateDeliveryInfo(order, dto.getCourierCountry(), dto.getTrackingNumber());
    }
}
