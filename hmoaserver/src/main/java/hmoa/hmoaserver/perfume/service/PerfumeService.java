package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeDefaultResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSaveRequestDto;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private final PerfumeLikedMemberService perfumeLikedMemberService;

    public Perfume save(PerfumeSaveRequestDto requestDto) {
        Brand brand = brandRepository.findByBrandName(requestDto.getBrandName())
                        .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));

        return perfumeRepository.save(requestDto.toEntity(brand));
    }

    public Perfume findById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new CustomException(null, PERFUME_NOT_FOUND));
    }

    public Long addPerfumeLikes(Perfume perfume, Member member) {
        Long savedPerfumeLikedMemberId = perfumeLikedMemberService.save(perfume, member);
        try {
            perfume.increaseHeartCount();
            perfumeRepository.save(perfume);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
        return savedPerfumeLikedMemberId;
    }

    public void deletePerfumeLikes(Perfume perfume, Member member) {
        if (perfume.getHeartCount() <= 0) {
            throw new CustomException(null, HEART_NOT_FOUND);
        }
        perfumeLikedMemberService.deleteById(perfume, member);

        try {
            perfume.decreaseHeartCount();
            perfumeRepository.save(perfume);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

}
