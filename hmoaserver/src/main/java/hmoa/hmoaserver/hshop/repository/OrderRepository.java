package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findById(Long id);
}
