package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.repository.PerfumeReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeReviewService {
    private final PerfumeReviewRepository perfumeReviewRepository;
    public PerfumeReview findPerfumeReview(Perfume perfume){
        return perfumeReviewRepository.findByPerfume(perfume).orElseThrow(()->new CustomException(null,Code.REVIEW_NOT_FOUND));
    }
    public boolean isPresentPerfumeReview(Perfume perfume){
        try {
            return perfumeReviewRepository.findByPerfume(perfume).isPresent();
        } catch (RuntimeException e){
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
    public void intialSaveReview(Perfume perfume){
        if(!isPresentPerfumeReview(perfume)){
            PerfumeReview perfumeReview = PerfumeReview.builder()
                    .perfume(perfume)
                    .build();
            perfumeReviewRepository.save(perfumeReview);
        }
    }
}
