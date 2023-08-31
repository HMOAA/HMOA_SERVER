package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.domain.PerfumeWeather;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherRequestDto;
import hmoa.hmoaserver.perfume.review.repository.PerfumeReviewRepository;
import hmoa.hmoaserver.perfume.review.repository.PerfumeWeatherRepository;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeWeatherService {
    private final PerfumeWeatherRepository perfumeWeatherRepository;
    private final PerfumeReviewService perfumeReviewService;
    private final MemberService memberService;
    private final PerfumeService perfumeService;
    private final JwtService jwtService;

    public boolean isPresentPerfumeWeather(Member member, Perfume perfume) {
        try {
            return perfumeWeatherRepository.findByMemberAndPerfume(member, perfume).isPresent();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public void save(String token, Long perfumeId, PerfumeWeatherRequestDto dto){
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        Perfume perfume = perfumeService.findById(perfumeId);
        if(!isPresentPerfumeWeather(member,perfume)){
            perfumeReviewService.intialSaveReview(perfume);
            PerfumeWeather perfumeWeather = PerfumeWeather.builder()
                    .perfume(perfume)
                    .member(member)
                    .weatherIndex(dto.getWeather())
                    .build();
            reflectWeatherToReview(dto.getWeather(),perfume);
            perfumeWeatherRepository.save(perfumeWeather);
        }else {
            PerfumeWeather perfumeWeather = perfumeWeatherRepository.findByMemberAndPerfume(member,perfume).get();
            int idx = perfumeWeather.getWeatherIndex();
            modifyWeatherToReview(idx,perfume);
            perfumeWeather.updateWeatherIndex(dto.getWeather());
            reflectWeatherToReview(dto.getWeather(),perfume);
        }
    }
    public void reflectWeatherToReview(int weather,Perfume perfume){
        PerfumeReview perfumeReview = perfumeReviewService.findPerfumeReview(perfume);
        if (weather==1){
            perfumeReview.increaseSpring();
        } else if (weather==2) {
            perfumeReview.increaseSummer();
        } else if (weather==3) {
            perfumeReview.increaseAutumn();
        } else if (weather==4) {
            perfumeReview.increaseWinter();
        }else throw new CustomException(null,SERVER_ERROR);
    }

    public void modifyWeatherToReview(int weather,Perfume perfume){
        PerfumeReview perfumeReview = perfumeReviewService.findPerfumeReview(perfume);
        if (weather==1){
            perfumeReview.decreaseSpring();
        } else if (weather==2) {
            perfumeReview.decreaseSummer();
        } else if (weather==3) {
            perfumeReview.decreaseAutumn();
        } else if (weather==4) {
            perfumeReview.decreaseWinter();
        }else throw new CustomException(null,SERVER_ERROR);
    }

}
