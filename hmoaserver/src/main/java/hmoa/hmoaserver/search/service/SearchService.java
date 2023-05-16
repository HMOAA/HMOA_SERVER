package hmoa.hmoaserver.search.service;


import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
    private final BrandRepository brandRepository;

    public List<BrandDefaultResponseDto> brandSearchAll(){
        List<Brand> brands= brandRepository.findAll();
        List<BrandDefaultResponseDto> dto = brands.stream().map(brand-> new BrandDefaultResponseDto(brand)).collect(Collectors.toList());
        return dto;
    }

    public List<BrandDefaultResponseDto> brandSearch(String brandName, String englishName){
        Pageable pageable= PageRequest.of(0,10);
        Page<Brand> brands = brandRepository.findAllSearch(brandName,englishName,pageable);
        List<BrandDefaultResponseDto> dto = brands.stream().map(brand -> new BrandDefaultResponseDto(brand)).collect(Collectors.toList());
        return dto;
    }
}
