package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeDefaultResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSaveRequestDto;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.BRAND_NOT_FOUND;
import static hmoa.hmoaserver.exception.Code.PERFUME_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

    public Perfume save(PerfumeSaveRequestDto requestDto) {
        Brand brand = brandRepository.findByBrandName(requestDto.getBrandName())
                        .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));

        return perfumeRepository.save(requestDto.toEntity(brand));
    }

    public Perfume findById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new CustomException(null, PERFUME_NOT_FOUND));
    }


}
