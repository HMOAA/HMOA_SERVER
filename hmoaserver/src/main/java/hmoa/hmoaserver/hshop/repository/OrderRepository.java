package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findById(Long id);
    List<OrderEntity> findByMemberId(Long memberId);
    Page<OrderEntity> findByMemberId(Long memberId, Pageable pageable);
}
