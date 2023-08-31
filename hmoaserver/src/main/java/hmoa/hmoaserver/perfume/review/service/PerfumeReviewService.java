package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.repository.PerfumeReviewRepository;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeReviewService {
    private final PerfumeReviewRepository perfumeReviewRepository;
    private final PerfumeService perfumeService;
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

    public List<Double> calcurateWeather(Long perfumeId){
        Perfume perfume = perfumeService.findById(perfumeId);
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> weather= List.of(perfumeReview.getSpring(),perfumeReview.getSummer(),perfumeReview.getAutumn(),perfumeReview.getWinter());
        int sum = weather.stream().mapToInt(Integer::intValue).sum();
        List<Double> result = new ArrayList<>();
        if (sum==0){return result;}
        for (int cur : weather){
            if(cur==0){
                result.add(0.0);
            }else {
                result.add(calcuratePercentage((double) sum,(double) cur));
            }
        }
        return result;

//        if (spring==0 && summer==0 && autumn==0 && winter==0){
//        }else {
//            double sum = spring+summer+autumn+winter;
//            if(spring!=0) calcuratePercentage(sum,spring);
//            if(summer!=0) calcuratePercentage(sum,summer);
//            if(autumn!=0) calcuratePercentage(sum,autumn);
//            if(winter!=0) calcuratePercentage(sum,au)
//        }
    }

    public double calcuratePercentage(double sum , double cur){
        return cur/sum*100.0;
    }
}
