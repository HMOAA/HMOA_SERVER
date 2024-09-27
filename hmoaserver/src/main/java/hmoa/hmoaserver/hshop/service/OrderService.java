package hmoa.hmoaserver.hshop.service;


import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.repository.OrderRepository;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderEntity save (OrderEntity order) {
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public OrderEntity findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomException(null, Code.ORDER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public OrderEntity getByTrackingNumber(String trackingNumber) {
        return orderRepository.findByTrackingNumber(trackingNumber).orElseThrow(() -> new CustomException(null, Code.ORDER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> findByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Page<OrderEntity> findByMemberId(Long memberId, Long cursor) {
        return orderRepository.findByMemberIdAndStatusNot(memberId, OrderStatus.getCancelStatus(), cursor, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.FIVE_SIZE.getSize()));
    }

    @Transactional(readOnly = true)
    public Page<OrderEntity> findCancelByMemberId(Long memberId, Long cursor) {
        return orderRepository.findByMemberIdAndStatus(memberId, OrderStatus.getCancelStatus(), cursor, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.FIVE_SIZE.getSize()));
    }

    public void deleteOrders(List<OrderEntity> orders) {
        orderRepository.deleteAll(orders);
    }

    public void deleteOrder(OrderEntity order) {
        orderRepository.delete(order);
    }

    public void updateOrderStatus(OrderEntity order, OrderStatus status) {
        order.updateOrderStatus(status);
    }

    public void updateOrderAddress(OrderEntity order, Long addressId) {
        order.updateAddressId(addressId);
    }

    public void updateOrderReceiptId(OrderEntity order, String receiptId) {
        order.updateReceiptId(receiptId);
    }

    public void updateDeliveryInfo(OrderEntity order, String courierCountry, String trackingNumber) {
        order.updateCourierCompany(courierCountry);
        order.updateTrackingNumber(trackingNumber);
    }

    public void deleteProduct(OrderEntity order, final NoteProduct product) {

        if (order.getProductIds().contains(product.getId())) {
            order.deleteProductId(product);
        }
    }

    public OrderEntity firstOrderSave(Member member, List<Long> productIds, int totalPrice) {
        return save(OrderEntity.builder()
                .memberId(member.getId())
                .status(OrderStatus.CREATED)
                .totalPrice(totalPrice)
                .productIds(productIds)
                .build());
    }
}
