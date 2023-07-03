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

import static hmoa.hmoaserver.exception.Code.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeLikedMemberService {

    private final PerfumeLikedMemberRepository perfumeLikedMemberRepository;

    public boolean isMemberLikedPerfume(Perfume perfume, Member member) {
        return perfumeLikedMemberRepository.existsByMemberIdAndPerfumeId(perfume.getId(), member.getId());
    }

    public Long save(Perfume perfume, Member member) {

        boolean perfumeLikedYn = isMemberLikedPerfume(perfume, member);
        if (perfumeLikedYn == true) {
            throw new CustomException(null, DUPLICATE_LIKED);
        }

        try {
            PerfumeLikedMember perfumeLikedMember = PerfumeLikedMember.builder()
                    .member(member)
                    .perfume(perfume)
                    .build();

            perfumeLikedMemberRepository.save(perfumeLikedMember);
            return perfumeLikedMember.getId();
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public void deleteById(Perfume perfume, Member member) {
        boolean likedYn = isMemberLikedPerfume(perfume, member);

        if (likedYn == false) {
            throw new CustomException(null, HEART_NOT_FOUND);
        }

        try {
            perfumeLikedMemberRepository.deleteByMemberIdAndPerfumeId(member.getId(), perfume.getId());
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }
}
