package hmoa.hmoaserver.brand.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandSaveRequestDto;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;

    public Brand save(BrandSaveRequestDto requestDto) {
        return brandRepository.save(requestDto.toEntity());
    }

    public Brand findById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));
    }

}
