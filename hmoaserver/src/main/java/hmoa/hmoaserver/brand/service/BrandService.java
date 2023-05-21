package hmoa.hmoaserver.brand.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandSaveRequestDto;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandLikedMemberService brandLikedMemberService;

    public Brand save(BrandSaveRequestDto requestDto) {
        return brandRepository.save(requestDto.toEntity());
    }

    public Brand findById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));
    }

    public Long addPostLikes(Brand brand, Member member) {

        Long savedBrandLikedMemberId = brandLikedMemberService.save(brand, member);
        try {
            brand.increaseHeartCount();
            brandRepository.save(brand);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
        return savedBrandLikedMemberId;
    }

    public void deleteBrandLikes(Brand brand, Member member) {

        if(brand.getHeartCount() <= 0) {
            throw new CustomException(null, HEART_NOT_FOUND);
        }
        brandLikedMemberService.deleteById(brand, member);

        try {
            brand.decreaseHeartCount();
            brandRepository.save(brand);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }
}
