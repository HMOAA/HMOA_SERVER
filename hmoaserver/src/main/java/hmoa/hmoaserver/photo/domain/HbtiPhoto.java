package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.hshop.domain.HbtiReview;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HbtiPhoto extends BasePhoto<HbtiReview> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hbti_review_id")
    private HbtiReview hbtiReview;

    @Override
    public HbtiReview getEntity() {
        return this.hbtiReview;
    }

    @Builder
    public HbtiPhoto(HbtiReview hbtiReview, String photoUrl) {
        super(photoUrl);
        this.hbtiReview = hbtiReview;
    }
}
