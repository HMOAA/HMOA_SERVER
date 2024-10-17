package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.hshop.domain.HbtiReview;

import lombok.*;

@Data
public class HbtiReviewSaveRequestDto {

    private String content;

    public HbtiReview toEntity(Long memberId, Long orderId) {
        return HbtiReview.builder()
                .content(content)
                .orderId(orderId)
                .memberId(memberId)
                .build();
    }
}
