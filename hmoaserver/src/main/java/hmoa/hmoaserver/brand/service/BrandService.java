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

import java.util.List;

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

    public Brand newSave(String brandName, String brandEnglishName) {
        int num = unicodeService.extractIntialChar(brandName);
        brandName = removeSpace(brandName);
        if (brandRepository.findByBrandName(brandName).isPresent()) {
            log.info("skip");
            return null;
        }

        return brandRepository.save(Brand.builder().brandName(brandName).consonant(num).englishName(brandEnglishName).build());
    }

    public Brand findById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    private String removeSpace(String str) {
        String result = str.replaceAll(" ", "");
        return result;
    }
}
