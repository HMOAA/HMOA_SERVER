package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findById(Long id);
    List<OrderEntity> findByMemberId(Long memberId);

    @Query("SELECT o " +
            "FROM OrderEntity o " +
            "WHERE o.memberId = :memberId AND o.status <> :status AND o.id < :cursor " +
            "ORDER BY o.createdAt DESC, o.id DESC")
    Page<OrderEntity> findByMemberIdAndStatusNot(Long memberId, OrderStatus status, Long cursor, Pageable pageable);

    @Query("SELECT o " +
            "FROM OrderEntity o " +
            "WHERE o.memberId = :memberId AND o.status = :status AND o.id < :cursor " +
            "ORDER BY o.createdAt DESC, o.id DESC")
    Page<OrderEntity> findByMemberIdAndStatus(Long memberId, OrderStatus status, Long cursor, Pageable pageable);
}
