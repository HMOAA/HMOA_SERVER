package hmoa.hmoaserver.search.service;


import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeSearchResponseDto;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import hmoa.hmoaserver.search.dto.SearchBrandResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
    private final BrandRepository brandRepository;
    private final PerfumeRepository perfumeRepository;
    private final UnicodeService unicodeService;

    public List<BrandDefaultResponseDto> brandSearchAll(){
        List<Brand> brands= brandRepository.findAll();
        List<BrandDefaultResponseDto> dto = brands.stream().map(brand-> new BrandDefaultResponseDto(brand)).collect(Collectors.toList());
        return dto;
    }

    public List<SearchBrandResponseDto> brandSearch(String brandName, String englishName, int page){
        Pageable pageable= PageRequest.of(page,10);
        Page<Brand> brands = brandRepository.findAllSearch(brandName,englishName,pageable);
        List<BrandDefaultResponseDto> brandList = new ArrayList<>();
        List<SearchBrandResponseDto> searchBrandList = new ArrayList<>();
        int temp = 0;
        for (Brand brand : brands){
            log.info("{},{}",brand.getBrandName(),unicodeService.extractIntialChar(brand.getBrandName()));
            int num = unicodeService.extractIntialChar(brand.getBrandName());
            if (temp==num){
                BrandDefaultResponseDto dto = new BrandDefaultResponseDto(brand);
                brandList.add(dto);
            }else{
                SearchBrandResponseDto dto2 = SearchBrandResponseDto.builder()
                        .consonant(temp)
                        .brandList(brandList)
                        .build();
                brandList=new ArrayList<>();
                BrandDefaultResponseDto dto = new BrandDefaultResponseDto(brand);
                brandList.add(dto);
                temp=num;
                searchBrandList.add(dto2);
            }
        }
        searchBrandList.add(SearchBrandResponseDto.builder()
                .consonant(temp)
                .brandList(brandList)
                .build());
        return searchBrandList;
    }

    public List<PerfumeSearchResponseDto> perfumeSearch(String perfumeName, String englishName,int page){
        Pageable pageable = PageRequest.of(page,10);
        Page<Perfume> perfumes = perfumeRepository.findAllSearch(perfumeName,englishName,pageable);
        List<PerfumeSearchResponseDto> dto = perfumes.stream().map(perfume -> new PerfumeSearchResponseDto(perfume)).collect(Collectors.toList());
        return dto;
    }
}
