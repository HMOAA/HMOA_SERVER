package hmoa.hmoaserver.perfume.review.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeAge;
import hmoa.hmoaserver.perfume.review.domain.PerfumeGender;
import hmoa.hmoaserver.perfume.review.domain.PerfumeReview;
import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeRequestDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderRequestDto;
import hmoa.hmoaserver.perfume.review.repository.PerfumeAgeRepository;

import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.REVIEW_NOT_FOUND;
import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeAgeService {
    private final PerfumeReviewService perfumeReviewService;
    private final PerfumeAgeRepository perfumeAgeRepository;
    private final MemberService memberService;
    private final PerfumeService perfumeService;
    private final JwtService jwtService;

    private boolean isPresentPerfumeAge(Member member, Perfume perfume) {
        try {
            return perfumeAgeRepository.findByMemberAndPerfume(member, perfume).isPresent();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public PerfumeAgeResponseDto deletePerfumeAge(Member member, Perfume perfume){
        PerfumeAge perfumeAge = perfumeAgeRepository.findByMemberAndPerfume(member, perfume).orElseThrow(() -> new CustomException(null, REVIEW_NOT_FOUND));
        modifyAgeToReview(perfumeAge.getAgeRange(),perfume);
        perfumeAgeRepository.delete(perfumeAge);
        return new PerfumeAgeResponseDto(perfumeReviewService.calcurateAge(perfume), false);
    }

    public PerfumeAgeResponseDto save(String token, Long perfumeId, PerfumeAgeRequestDto dto){
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        Perfume perfume = perfumeService.findById(perfumeId);
        if(!isPresentPerfumeAge(member,perfume)){
            perfumeReviewService.intialSaveReview(perfume);
            PerfumeAge perfumeAge = PerfumeAge.builder()
                    .perfume(perfume)
                    .member(member)
                    .ageRange(dto.getAge())
                    .build();
            reflectAgeToReview(dto.getAge(),perfume);
            perfumeAgeRepository.save(perfumeAge);
            return new PerfumeAgeResponseDto(perfumeReviewService.calcurateAge(perfume),true);
        }else {
            PerfumeAge perfumeAge = perfumeAgeRepository.findByMemberAndPerfume(member,perfume).get();
            int idx = perfumeAge.getAgeRange();
            modifyAgeToReview(idx,perfume);
            perfumeAge.updateAgeRange(dto.getAge());
            reflectAgeToReview(dto.getAge(),perfume);
            return new PerfumeAgeResponseDto(perfumeReviewService.calcurateAge(perfume),true);
        }
    }

    private void reflectAgeToReview(int age,Perfume perfume){
        PerfumeReview perfumeReview = perfumeReviewService.findPerfumeReview(perfume);
        if (age==1){
            perfumeReview.increaseTen();
        } else if (age==2) {
            perfumeReview.increaseTwenty();
        } else if (age==3) {
            perfumeReview.increaseThirty();
        }else if (age==4) {
            perfumeReview.increaseFourty();
        }else if (age==5) {
            perfumeReview.increaseFifty();
        }else throw new CustomException(null,SERVER_ERROR);
    }

    private void modifyAgeToReview(int age,Perfume perfume){
        PerfumeReview perfumeReview = perfumeReviewService.findPerfumeReview(perfume);
        if (age==1){
            perfumeReview.decreaseTen();
        } else if (age==2) {
            perfumeReview.decreaseTwenty();
        } else if (age==3) {
            perfumeReview.decreaseThirty();
        }else if (age==4) {
            perfumeReview.decreaseFourty();
        }else if (age==5) {
            perfumeReview.decreaseFifty();
        }else throw new CustomException(null,SERVER_ERROR);
    }
}
