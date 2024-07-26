package hmoa.hmoaserver.hshop.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.Cart;
import hmoa.hmoaserver.hshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    public Cart save(Cart cart) {
        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Cart> findOneCartByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }

    public void updateCart(Cart cart, List<Long> productIds, int totalPrice) {
        cart.updateProducts(productIds, totalPrice);
    }
}
