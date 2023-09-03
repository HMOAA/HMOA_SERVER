package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeGetSecondResponseDto;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherResponseDto;
import hmoa.hmoaserver.perfume.review.repository.PerfumeReviewRepository;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
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

    public PerfumeGetSecondResponseDto getReview(Long perfumeId){
        Perfume perfume = perfumeService.findById(perfumeId);
        List<Double> weather = calcurateWeather(perfume);
        List<Double> age = calcurateAge(perfume);
        List<Double> gender = calcurateGender(perfume);
        PerfumeWeatherResponseDto weatherDto = new PerfumeWeatherResponseDto(weather);
        PerfumeAgeResponseDto ageDto = new PerfumeAgeResponseDto(age);
        PerfumeGenderResponseDto genderDto = new PerfumeGenderResponseDto(gender);

        return new PerfumeGetSecondResponseDto(ageDto,weatherDto,genderDto);

    }

    /**
     * 계절감 백분율 구하기
     */

    public List<Double> calcurateWeather(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> weathers= List.of(perfumeReview.getSpring(),perfumeReview.getSummer(),perfumeReview.getAutumn(),perfumeReview.getWinter());
        return calcurateReview(weathers);
    }

    /**
     * 성별 백분율 구하기
     */
    public List<Double> calcurateGender(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> genders = List.of(perfumeReview.getMan(),perfumeReview.getWoman(),perfumeReview.getNeuter());
        return calcurateReview(genders);
    }

    /**
     * 나이 백분율 구하기
     */
    public List<Double> calcurateAge(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> ages = List.of(perfumeReview.getTen(),perfumeReview.getTwenty(),perfumeReview.getThirty(),perfumeReview.getFourty(),perfumeReview.getFifty());
        return calcurateReview(ages);
    }

    /**
     * 성별, 계절감, 나이 백분율 계산 함수
     */
    public List<Double> calcurateReview(List<Integer> reviews){
        int sum = reviews.stream().mapToInt(Integer::intValue).sum();
        List<Double> result = new ArrayList<>();
        if (sum==0) return result;
        for (int review : reviews){
            if (review==0){
                result.add(0.0);
            }else {
                result.add(calcuratePercentage((double)sum,(double) review));
            }
        }
        return result;
    }
    public double calcuratePercentage(double sum , double cur){
        return cur/sum*100.0;
    }
}
