package hmoa.hmoaserver.hshop.repository;

import hmoa.hmoaserver.hshop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByMemberId(Long memberId);
}
