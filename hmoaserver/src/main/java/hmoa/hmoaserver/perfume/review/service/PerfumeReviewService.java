package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.domain.PerfumeWeather;
import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherResponseDto;
import hmoa.hmoaserver.perfume.review.repository.PerfumeAgeRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeGenderRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeReviewRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeWeatherRepository;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Double> weather = calculateWeather(perfume);
        Double age = calculateAge(perfume);
        List<Double> gender = calculateGender(perfume);
        PerfumeWeatherResponseDto weatherDto = new PerfumeWeatherResponseDto(weather,false, 0);
        PerfumeAgeResponseDto ageDto = new PerfumeAgeResponseDto(age,false);
        PerfumeGenderResponseDto genderDto = new PerfumeGenderResponseDto(gender, false, 0);

        return new PerfumeReviewResponseDto(ageDto,weatherDto,genderDto);

    }

    /**
     * 회원이 리뷰 조회
     */
    public PerfumeReviewResponseDto getReview(Long perfumeId, Member member){
        Perfume perfume = perfumeService.findById(perfumeId);
        List<Double> weather = calculateWeather(perfume);
        Double age = calculateAge(perfume);
        Map<String, Integer> selectedList = selectedReview(perfume, member);
        log.info("1");
        List<Double> gender = calculateGender(perfume);
        log.info("2");
        PerfumeWeatherResponseDto weatherDto = getWeatherDto(selectedList, weather);
        log.info("3");
        PerfumeAgeResponseDto ageDto = new PerfumeAgeResponseDto(age, isWritedAge(perfume, member));
        log.info("4");
        PerfumeGenderResponseDto genderDto = getGenderResponseDto(selectedList, gender);
        log.info("5");
        return new PerfumeReviewResponseDto(ageDto,weatherDto,genderDto);
    }

    private PerfumeGenderResponseDto getGenderResponseDto(Map<String, Integer> selectedList, List<Double> gender) {
        if (selectedList.containsKey("gender")) {
            return new PerfumeGenderResponseDto(gender, true, selectedList.get("gender"));
        }
        return new PerfumeGenderResponseDto(gender, false, 0);
    }

    private PerfumeWeatherResponseDto getWeatherDto(Map<String, Integer> selectedList, List<Double> weather) {
        if (selectedList.containsKey("weather")) {
            return new PerfumeWeatherResponseDto(weather, true, selectedList.get("weather"));
        }
        return new PerfumeWeatherResponseDto(weather, false, 0);
    }

    private boolean isWritedAge(Perfume perfume,Member member){
        return perfumeAgeRepository.findByMemberAndPerfume(member,perfume).isPresent();
    }

    private Map<String, Integer> selectedReview(Perfume perfume, Member member) {
        Map<String, Integer> selectedList = new HashMap<>();
        if (perfumeWeatherRepository.findByMemberAndPerfume(member, perfume).isPresent()) {
            selectedList.put("weather", perfumeWeatherRepository.findByMemberAndPerfume(member, perfume).get().getWeatherIndex());
        }
        if (perfumeGenderRepository.findByMemberAndPerfume(member, perfume).isPresent()) {
            selectedList.put("gender", perfumeGenderRepository.findByMemberAndPerfume(member, perfume).get().getGender());
        }
        return selectedList;
    }

    /**
     * 계절감 백분율 구하기
     */
    public List<Double> calculateWeather(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> weathers= List.of(perfumeReview.getSpring(),perfumeReview.getSummer(),perfumeReview.getAutumn(),perfumeReview.getWinter());
        return calcurateReview(weathers);
    }

    /**
     * 성별 백분율 구하기
     */
    public List<Double> calculateGender(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> genders = List.of(perfumeReview.getMan(), perfumeReview.getWoman(), perfumeReview.getNeuter());
        return calcurateReview(genders);
    }

    /**
     * 나이 평균 구하기
     */
    public Double calculateAge(Perfume perfume){
        PerfumeReview perfumeReview = findPerfumeReview(perfume);
        List<Integer> ages = List.of(10*perfumeReview.getTen(), 20*perfumeReview.getTwenty(), 30*perfumeReview.getThirty(), 40*perfumeReview.getFourty(), 50*perfumeReview.getFifty());

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
                result.add(calcuratePercentage((double)sum, (double) review));
            }
        }
        return result;
    }
    public double calcuratePercentage(double sum , double cur){
        return cur/sum * 100.0;
    }
}
