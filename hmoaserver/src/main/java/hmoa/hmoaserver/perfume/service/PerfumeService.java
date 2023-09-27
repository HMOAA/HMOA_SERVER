package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeSaveRequestDto;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

    public Perfume save(PerfumeSaveRequestDto requestDto) {
        Brand brand = brandRepository.findByBrandName(requestDto.getABrandName())
                        .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));

        return perfumeRepository.save(requestDto.toEntity(brand));
    }

    /**
     *  향수 저장 테스트 서비스
     */
    public Perfume testSave(PerfumeSaveRequestDto dto){
        Brand brand = brandRepository.findByBrandName(dto.getABrandName())
                .orElseThrow(()-> new CustomException(null,BRAND_NOT_FOUND));
        return perfumeRepository.save(dto.toEntity(brand));
    }

    /**
     *  향수 단건 조회
     */
    public Perfume findById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new CustomException(null, PERFUME_NOT_FOUND));
    }

    /**
     *  향수 조회(최신순) 보류
     */
    public Page<Perfume> findUpdatePerfumesByBrand(Long brandId, int pageNum) {
        try {
            Page<Perfume> foundPerfumes =
                    perfumeRepository.findAllByBrandIdOrderByCreatedAtDesc(
                            brandId,
                            PageRequest.of(pageNum, 6)
                    );
            return foundPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     *  향수 조회(문자열순)
     */
    public Page<Perfume> findPerfumesByBrand(Long brandId, int pageNum) {
        try {
            Page<Perfume> foundPerfumes =
                    perfumeRepository.findAllByBrandIdOrderByKoreanName(
                            brandId,
                            PageRequest.of(pageNum, 6)
                    );
            return foundPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     *  향수 조회(좋아요순)
     */
    public Page<Perfume> findTopPerfumesByBrand(Long brandId, int pageNum) {
        try {
            Page<Perfume> foundPerfumes =
                    perfumeRepository.findAllByBrandIdOrderByHeartCountDesc(
                            brandId,
                            PageRequest.of(pageNum, 6)
                    );
            return foundPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

}
