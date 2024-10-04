package hmoa.hmoaserver.hshop.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HbtiReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hbti_review_id")
    private Long id;

    private Long memberId;
    private Long orderId;
    private String content;
    private int heartCount;

    @Builder
    public HbtiReview(Long id, Long memberId, Long orderId, String content) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.content = content;
        this.heartCount = 0;
    }
}
