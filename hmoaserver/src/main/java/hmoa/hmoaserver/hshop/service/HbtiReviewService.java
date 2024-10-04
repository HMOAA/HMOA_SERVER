package hmoa.hmoaserver.hshop.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.HbtiReview;
import hmoa.hmoaserver.hshop.domain.HbtiReviewHeart;
import hmoa.hmoaserver.hshop.repository.HbtiReviewHeartRepository;
import hmoa.hmoaserver.hshop.repository.HbtiReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HbtiReviewService {

    private final HbtiReviewRepository hbtiReviewRepository;
    private final HbtiReviewHeartRepository hbtiReviewHeartRepository;

    @Transactional
    public void save(HbtiReview hbtiReview) {
        try {
            hbtiReviewRepository.save(hbtiReview);
        } catch (Exception e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    @Transactional
    public void saveHeart(Long reviewId, Long memberId) {
        if (isPresentReviewHeart(reviewId, memberId)) {
            throw new CustomException(null, Code.DUPLICATE_LIKED);
        }

        try {
            hbtiReviewHeartRepository.save(HbtiReviewHeart.builder()
                    .hbtiReviewId(reviewId)
                    .memberId(memberId)
                    .build());
        } catch (Exception e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    @Transactional
    public void deleteHeart(Long reviewId, Long memberId) {
        Optional<HbtiReviewHeart> heart = hbtiReviewHeartRepository.findByHbtiReviewIdAndMemberId(reviewId, memberId);
        heart.ifPresent(hbtiReviewHeartRepository::delete);
    }

    @Transactional(readOnly = true)
    public HbtiReview getReview(Long reviewId) {
        return hbtiReviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(null, Code.HBTI_REVIEW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean isPresentReviewHeart(Long reviewId, Long memberId) {
        return hbtiReviewHeartRepository.findByHbtiReviewIdAndMemberId(reviewId, memberId).isPresent();
    }
}
