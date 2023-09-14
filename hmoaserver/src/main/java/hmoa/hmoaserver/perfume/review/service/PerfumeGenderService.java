package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeGender;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderRequestDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderResponseDto;
import hmoa.hmoaserver.perfume.review.repository.PerfumeGenderRepository;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.DUPLICATE_GENDERIDX;
import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeGenderService {
    private final PerfumeReviewService perfumeReviewService;
    private final PerfumeGenderRepository perfumeGenderRepository;
    private final MemberService memberService;
    private final PerfumeService perfumeService;
    private final JwtService jwtService;

    public boolean isPresentPerfumeGender(Member member, Perfume perfume) {
        try {
            return perfumeGenderRepository.findByMemberAndPerfume(member, perfume).isPresent();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
    public PerfumeGenderResponseDto save(String token, Long perfumeId, PerfumeGenderRequestDto dto){
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        Perfume perfume = perfumeService.findById(perfumeId);
        if(!isPresentPerfumeGender(member,perfume)){
            perfumeReviewService.intialSaveReview(perfume);
            PerfumeGender perfumeGender = PerfumeGender.builder()
                    .perfume(perfume)
                    .member(member)
                    .gender(dto.getGender())
                    .build();
            reflectGenderToReview(dto.getGender(),perfume);
            perfumeGenderRepository.save(perfumeGender);
            return new PerfumeGenderResponseDto(perfumeReviewService.calcurateGender(perfume),true);
        }else {
            PerfumeGender perfumeGender = perfumeGenderRepository.findByMemberAndPerfume(member,perfume).get();
            int idx = perfumeGender.getGender();
            modifyGenderToReview(idx,perfume);
            perfumeGender.updateGender(dto.getGender());
            reflectGenderToReview(dto.getGender(),perfume);
            return new PerfumeGenderResponseDto(perfumeReviewService.calcurateGender(perfume),true);
        }
    }

    public void reflectGenderToReview(int gender,Perfume perfume){
        PerfumeReview perfumeReview = perfumeReviewService.findPerfumeReview(perfume);
        if (gender==1){
            perfumeReview.increaseMan();
        } else if (gender==2) {
            perfumeReview.increaseWoman();
        } else if (gender==3) {
            perfumeReview.increaseNeuter();
        }else throw new CustomException(null,SERVER_ERROR);
    }

    public void modifyGenderToReview(int gender,Perfume perfume){
        PerfumeReview perfumeReview = perfumeReviewService.findPerfumeReview(perfume);
        if (gender==1){
            perfumeReview.decreaseMan();
        } else if (gender==2) {
            perfumeReview.decreaseWoman();
        } else if (gender==3) {
            perfumeReview.decreaseNeuter();
        } else throw new CustomException(null,SERVER_ERROR);
    }
}
