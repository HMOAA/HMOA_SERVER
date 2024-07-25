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
    List<Integer> productIds;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long addressId;
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public OrderEntity(List<Integer> productIds, OrderStatus status, Long addressId, int totalPrice, Member member) {
        this.productIds = productIds;
        this.status = status;
        this.addressId = addressId;
        this.totalPrice = totalPrice;
        this.member = member;
    }
}
