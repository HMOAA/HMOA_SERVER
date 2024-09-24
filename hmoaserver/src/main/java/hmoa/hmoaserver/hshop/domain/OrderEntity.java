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
    private String receiptId;
    private String courierCompany;
    private String trackingNumber;

    @Builder
    public OrderEntity(List<Long> productIds, OrderStatus status, Long addressId, int totalPrice, Long memberId, String receiptId, String courierCompany, String trackingNumber) {
        this.productIds = productIds;
        this.status = status;
        this.addressId = addressId;
        this.totalPrice = totalPrice;
        this.memberId = memberId;
        this.receiptId = receiptId;
        this.courierCompany = courierCompany;
        this.trackingNumber = trackingNumber;
    }

    public void deleteProductId(final NoteProduct product) {
        this.productIds.remove(product.getId());
        this.totalPrice -= product.getPrice();
    }

    public void updateOrderStatus(final OrderStatus status) {
        this.status = status;
    }

    public void updateReceiptId(final String receiptId) {
        this.receiptId = receiptId;
    }

    public void updateCourierCompany(final String courierCompany) {
        this.courierCompany = courierCompany;
    }

    public void updateTrackingNumber(final String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void updateAddressId(final Long addressId) {
        this.addressId = addressId;
    }
}
