package hmoa.hmoaserver.hshop.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ElementCollection
    List<Long> productIds;

    private int totalPrice;

    @Column(unique = true, nullable = false)
    private Long memberId;

    @Builder
    public Cart(List<Long> productIds, int totalPrice, Long memberId) {
        this.productIds = productIds;
        this.totalPrice = totalPrice;
        this.memberId = memberId;
    }

    public void updateProducts(List<Long> productIds, int totalPrice) {
        this.productIds = productIds;
        this.totalPrice = totalPrice;
    }
}
