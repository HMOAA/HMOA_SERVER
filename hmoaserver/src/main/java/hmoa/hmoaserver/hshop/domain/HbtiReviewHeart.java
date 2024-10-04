package hmoa.hmoaserver.hshop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HbtiReviewHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hbti_review_heart_id")
    private Long id;

    private Long memberId;
    private Long hbtiReviewId;

    @Builder
    public HbtiReviewHeart(Long memberId, Long hbtiReviewId) {
        this.memberId = memberId;
        this.hbtiReviewId = hbtiReviewId;
    }
}
