package hmoa.hmoaserver.brand.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandSaveRequestDto;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.search.service.UnicodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;
    private final UnicodeService unicodeService;

    public Brand save(BrandSaveRequestDto requestDto) {
        int num = unicodeService.extractIntialChar(requestDto.getBrandName());
        log.info("{}",num);
        return brandRepository.save(requestDto.toEntity(num));
    }

    public Brand findById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));
    }

}
