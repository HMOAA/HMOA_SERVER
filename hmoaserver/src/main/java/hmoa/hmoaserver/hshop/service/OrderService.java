package hmoa.hmoaserver.hshop.service;


import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import hmoa.hmoaserver.hshop.repository.OrderRepository;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public OrderEntity firstOrderSave(Member member, List<Long> productIds, int totalPrice) {
        return save(OrderEntity.builder()
                .member(member)
                .status(OrderStatus.CREATED)
                .totalPrice(totalPrice)
                .productIds(productIds)
                .build());
    }
}
