package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeLikedMember;
import hmoa.hmoaserver.perfume.repository.PerfumeLikedMemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeLikedMemberService {

    private final PerfumeLikedMemberRepository perfumeLikedMemberRepository;

    public boolean isMemberLikedPerfume(Member member, Perfume perfume) {
        try {
            return perfumeLikedMemberRepository.findByMemberAndPerfume(member, perfume).isPresent();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }    }

    public PerfumeLikedMember save(Member member, Perfume perfume) {

        try {
            perfume.increaseHeartCount();
            PerfumeLikedMember perfumeLikedMember = PerfumeLikedMember.builder()
                    .member(member)
                    .perfume(perfume)
                    .build();

            return perfumeLikedMemberRepository.save(perfumeLikedMember);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public void delete(PerfumeLikedMember perfumeLikedMember) {

        try {
            perfumeLikedMemberRepository.delete(perfumeLikedMember);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public PerfumeLikedMember findOneByPerfumeAndMember(Perfume perfume, Member member) {
        return perfumeLikedMemberRepository.findByMemberAndPerfume(member, perfume)
                .orElseThrow(() -> new CustomException(null, PERFUMELIKEDMEMBER_NOT_FOUND));
    }

    public void decrementLikedCountsOfPerfume(Perfume perfume) {
        try {
            perfume.decreaseHeartCount();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public List<Long> findLikedPerfumeIdsByMemberId(Long memberId) {
        try {
            List<Long> foundLikedPerfumes = perfumeLikedMemberRepository.findPerfumeLikedMemberById(memberId);
            return foundLikedPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }
}
