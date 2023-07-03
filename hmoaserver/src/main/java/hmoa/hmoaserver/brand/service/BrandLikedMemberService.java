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

    public boolean isMemberLikedBrand(Brand brand, Member member) {
        return brandLikedMemberRepository.existsByMemberIdAndBrandId(brand.getId(), member.getId());
    }

    public Long save(Brand brand, Member member) {

        boolean brandLikedYn = isMemberLikedBrand(brand, member);
        if (brandLikedYn == true) {
            throw new CustomException(null, DUPLICATE_LIKED);
        }

        try {
            BrandLikedMember brandLikedMember = BrandLikedMember.builder()
                    .member(member)
                    .brand(brand)
                    .build();

            brandLikedMemberRepository.save(brandLikedMember);
            return brandLikedMember.getId();
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public void deleteById(Brand brand, Member member) {

        boolean likedYn = isMemberLikedBrand(brand, member);

        if(likedYn == false) {
            throw new CustomException(null, HEART_NOT_FOUND);
        }

        try {
            brandLikedMemberRepository.deleteByMemberIdAndBrandId(member.getId(), brand.getId());
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

}
