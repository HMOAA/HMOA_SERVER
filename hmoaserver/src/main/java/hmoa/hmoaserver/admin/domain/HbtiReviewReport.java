package hmoa.hmoaserver.admin.domain;

import hmoa.hmoaserver.hshop.domain.HbtiReview;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HbtiReviewReport extends BaseReport<HbtiReview> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hbti_review_id")
    private HbtiReview hbtiReview;

    @Override
    public HbtiReview getEntity() {
        return this.hbtiReview;
    }

    @Builder
    public HbtiReviewReport(HbtiReview hbtiReview, String reason) {
        super(reason);
        this.hbtiReview = hbtiReview;
    }
}
