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
    private final PerfumeReviewRepository perfumeReviewRepository;
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
            PerfumeWeather perfumeWeather = PerfumeWeather.builder()
                    .perfume(perfume)
                    .member(member)
                    .weatherIndex(dto.getWeather())
                    .build();
            perfumeWeatherRepository.save(perfumeWeather);
        }else {
            PerfumeWeather perfumeWeather = perfumeWeatherRepository.findByMemberAndPerfume(member,perfume).get();
            int idx = perfumeWeather.getWeatherIndex();
            perfumeWeather.updateWeatherIndex(dto.getWeather());

        }
    }

//    public String determineWeather(int weather){
//        if (weather==1){
//            return "spring";
//        } else if (weather==2) {
//            return "summer";
//        } else if (weather==3) {
//            return "autumn";
//        } else if (weather==4) {
//            return "winter";
//        }else throw new CustomException(null,SERVER_ERROR);
//    }
}
