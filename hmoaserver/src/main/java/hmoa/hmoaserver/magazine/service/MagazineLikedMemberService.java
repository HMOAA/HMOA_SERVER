package hmoa.hmoaserver.magazine.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.domain.MagazineLikedMember;
import hmoa.hmoaserver.magazine.repository.MagazineLikedMemberRepository;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MagazineLikedMemberService {
    private final MagazineLikedMemberRepository likedMemberRepository;

    @Transactional(readOnly = true)
    public boolean isMagazineLikedMember(Magazine magazine, Member member) {
        return likedMemberRepository.findByMagazineAndMember(magazine, member).isPresent();
    }

    public MagazineLikedMember save(Magazine magazine, Member member) {
        if (isMagazineLikedMember(magazine, member)) {
            throw new CustomException(null, DUPLICATE_LIKED);
        }

        try {
            MagazineLikedMember magazineLikedMember = likedMemberRepository.save(MagazineLikedMember.builder()
                    .magazine(magazine)
                    .member(member)
                    .build());
            magazine.increaseLikeCount();
            return magazineLikedMember;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public void delete(Magazine magazine, Member member) {
        MagazineLikedMember magazineLikedMember = findOneMagazineLikedMember(magazine, member);

        try {
            likedMemberRepository.delete(magazineLikedMember);
            magazine.decreaseLikeCount();
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public MagazineLikedMember findOneMagazineLikedMember(Magazine magazine, Member member) {
        return likedMemberRepository.findByMagazineAndMember(magazine, member).orElseThrow(() -> new CustomException(null, MAGAZINELIKEDMEMBER_NOT_FOUND));
    }
}
