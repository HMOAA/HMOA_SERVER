package hmoa.hmoaserver.hshop.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ElementCollection
    List<Long> productIds;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long addressId;
    private int totalPrice;

    private Long memberId;

    @Builder
    public OrderEntity(List<Long> productIds, OrderStatus status, Long addressId, int totalPrice, Long memberId) {
        this.productIds = productIds;
        this.status = status;
        this.addressId = addressId;
        this.totalPrice = totalPrice;
        this.memberId = memberId;
    }

    public void deleteProductId(final NoteProduct product) {
        this.productIds.remove(product.getId());
        this.totalPrice -= product.getPrice();
    }

    public void updateOrderStatus(final OrderStatus status) {
        this.status = status;
    }
}
