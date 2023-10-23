package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeDetailSecondResponseDto;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherResponseDto;
import hmoa.hmoaserver.perfume.review.repository.PerfumeAgeRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeGenderRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeReviewRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeWeatherRepository;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PerfumeReviewService {
    private final PerfumeReviewRepository perfumeReviewRepository;
    private final PerfumeService perfumeService;
    private final PerfumeWeatherRepository perfumeWeatherRepository;
    private final PerfumeAgeRepository perfumeAgeRepository;
    private final PerfumeGenderRepository perfumeGenderRepository;

    public PerfumeReview findPerfumeReview(Perfume perfume){
        intialSaveReview(perfume);
        return perfumeReviewRepository.findByPerfume(perfume).orElseThrow(()->new CustomException(null,Code.SERVER_ERROR));
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

    /**
     * 비회원이 리뷰 조회
     */
    public PerfumeReviewResponseDto getReview(Long perfumeId){
        Perfume perfume = perfumeService.findById(perfumeId);
        List<Double> weather = calcurateWeather(perfume);
        Double age = calcurateAge(perfume);
        List<Double> gender = calcurateGender(perfume);
        PerfumeWeatherResponseDto weatherDto = new PerfumeWeatherResponseDto(weather,false);
        PerfumeAgeResponseDto ageDto = new PerfumeAgeResponseDto(age,false);
        PerfumeGenderResponseDto genderDto = new PerfumeGenderResponseDto(gender,false);

        return new PerfumeReviewResponseDto(ageDto,weatherDto,genderDto);

    }

    /**
     * 회원이 리뷰 조회
     */
    public PerfumeReviewResponseDto getReview(Long perfumeId, Member member){
        Perfume perfume = perfumeService.findById(perfumeId);
        List<Boolean> isWritedList= isWritedReview(perfume,member);
        List<Double> weather = calcurateWeather(perfume);
        Double age = calcurateAge(perfume);
        List<Double> gender = calcurateGender(perfume);
        PerfumeWeatherResponseDto weatherDto = new PerfumeWeatherResponseDto(weather,isWritedList.get(0));
        PerfumeAgeResponseDto ageDto = new PerfumeAgeResponseDto(age,isWritedList.get(1));
        PerfumeGenderResponseDto genderDto = new PerfumeGenderResponseDto(gender,isWritedList.get(2));

        return new PerfumeReviewResponseDto(ageDto,weatherDto,genderDto);

    }
    private List<Boolean> isWritedReview(Perfume perfume,Member member){
        List<Boolean> isWritedList = new ArrayList<>();
        isWritedList.add(perfumeWeatherRepository.findByMemberAndPerfume(member,perfume).isPresent());
        isWritedList.add(perfumeAgeRepository.findByMemberAndPerfume(member,perfume).isPresent());
        isWritedList.add(perfumeGenderRepository.findByMemberAndPerfume(member,perfume).isPresent());
        return isWritedList;
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
        List<Integer> genders = List.of(perfumeReview.getMan(),perfumeReview.getWoman());
        return calcurateReview(genders);
    }

    /**
     * 나이 평균 구하기
     */
    public Double calcurateAge(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> ages = List.of(10*perfumeReview.getTen(),20*perfumeReview.getTwenty(),30*perfumeReview.getThirty(),40*perfumeReview.getFourty(),50*perfumeReview.getFifty());

        int sum = ages.stream().mapToInt(Integer::intValue).sum();
        int divisor = perfumeReview.getTen()+perfumeReview.getTwenty()+perfumeReview.getThirty()+perfumeReview.getFourty()+perfumeReview.getFifty();
        if (sum==0){
            return 0.0;
        }
        return (double) sum / (double) divisor;

    }

    /**
     * 성별, 계절감 백분율 계산 함수
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
