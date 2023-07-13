package hmoa.hmoaserver.brand.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.domain.BrandLikedMember;
import hmoa.hmoaserver.brand.repository.BrandLikedMemberRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static hmoa.hmoaserver.exception.Code.*;


@Service
@Transactional
@RequiredArgsConstructor
public class BrandLikedMemberService {

    private final BrandLikedMemberRepository brandLikedMemberRepository;

    public boolean isMemberLikedBrand(Member member, Brand brand) {
        try {
            return brandLikedMemberRepository.findByMemberAndBrand(member, brand).isPresent();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public BrandLikedMember save(Member member, Brand brand) {

        try {
            brand.increaseHeartCount();
            BrandLikedMember brandLikedMember = BrandLikedMember.builder()
                    .member(member)
                    .brand(brand)
                    .build();

            return brandLikedMemberRepository.save(brandLikedMember);
        } catch (RuntimeException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public void delete(BrandLikedMember brandLikedMember) {

        try {
            brandLikedMemberRepository.delete(brandLikedMember);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public BrandLikedMember findOneByBrandAndMember(Brand brand, Member member) {
        return brandLikedMemberRepository.findByMemberAndBrand(member, brand)
                .orElseThrow(() -> new CustomException(null, BRANDLIKEDMEMBER_NOT_FOUND));
    }

    public void decrementLikedCountsOfBrand(Brand brand) {
        try {
            brand.decreaseHeartCount();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
