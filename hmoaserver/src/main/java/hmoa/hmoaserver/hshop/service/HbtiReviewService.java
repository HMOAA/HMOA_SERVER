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
    public void saveHeart(HbtiReviewHeart hbtiReviewHeart) {
        try {
            hbtiReviewHeartRepository.save(hbtiReviewHeart);
        } catch (Exception e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
