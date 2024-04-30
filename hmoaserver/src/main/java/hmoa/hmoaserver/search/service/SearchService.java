package hmoa.hmoaserver.search.service;


import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.brandstory.domain.BrandStory;
import hmoa.hmoaserver.brandstory.repository.BrandStoryRepository;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.repository.NoteRepository;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfumer.domain.Perfumer;
import hmoa.hmoaserver.perfumer.repository.PerfumerRepository;
import hmoa.hmoaserver.search.dto.PerfumeSearchResponseDto;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import hmoa.hmoaserver.search.dto.BrandSearchResponseDto;
import hmoa.hmoaserver.search.dto.PerfumeNameSearchResponseDto;
import hmoa.hmoaserver.term.domain.Term;
import hmoa.hmoaserver.term.repository.TermRepository;
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
    private final CommunityRepository communityRepository;
    private final UnicodeService unicodeService;
    private final NoteRepository noteRepository;
    private final TermRepository termRepository;
    private final PerfumerRepository perfumerRepository;
    private final BrandStoryRepository brandStoryRepository;

    public List<BrandDefaultResponseDto> brandSearchAll(int consonant){
        List<Brand> brands= brandRepository.findAllByConsonant(consonant);
        List<BrandDefaultResponseDto> dto = brands.stream().map(brand-> new BrandDefaultResponseDto(brand)).collect(Collectors.toList());
        return dto;
    }

    public List<BrandSearchResponseDto> brandSearch(String brandName){
        List<Brand> brands = brandRepository.findAllSearch(brandName,brandName);
        List<BrandDefaultResponseDto> brandList = new ArrayList<>();
        List<BrandSearchResponseDto> searchBrandList = new ArrayList<>();
        int temp = 0;
        for (Brand brand : brands){
            log.info("{},{}",brand.getBrandName(),unicodeService.extractIntialChar(brand.getBrandName()));
            int num = unicodeService.extractIntialChar(brand.getBrandName());
            log.info("{}",num);
            if (temp==num){
                BrandDefaultResponseDto dto = new BrandDefaultResponseDto(brand);
                brandList.add(dto);
            } else if (temp==0) {
                temp=num;
                BrandDefaultResponseDto dto = new BrandDefaultResponseDto(brand);
                brandList.add(dto);
            } else{
                BrandSearchResponseDto dto2 = BrandSearchResponseDto.builder()
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
        searchBrandList.add(BrandSearchResponseDto.builder()
                .consonant(temp)
                .brandList(brandList)
                .build());
        return searchBrandList;
    }

    public Page<Perfume> perfumeSearch(String perfumeName, String englishName, int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return perfumeRepository.findAllSearch(perfumeName, englishName, pageable);
    }

    public List<PerfumeNameSearchResponseDto> perfumeNameSearch(String perfumeName, int page){
        Pageable pageable = PageRequest.of(page, 20);
        Page<Perfume> perfumes = perfumeRepository.findAllSearch(perfumeName, perfumeName, pageable);
        return perfumes.stream().map(PerfumeNameSearchResponseDto::new).collect(Collectors.toList());
    }

    public Page<Community> communitySearchForCategory(Category category, String searchContent, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return communityRepository.findByCategoryAndTitleContainingOrContentContainingOrderByCreatedAtDescIdAsc(
                category, searchContent, searchContent, pageable
        );
    }

    public Page<Community> communitySearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return communityRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDescIdAsc(
                keyword, keyword, pageable
        );
    }

    public Page<Note> noteSearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return noteRepository.findByTitleContainingOrSubtitleContainingOrderByCreatedAtDesc(
                keyword, keyword, pageable
        );
    }

    public Page<Term> termSearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return termRepository.findByTitleContainingOrEnglishTitleContainingOrderByCreatedAtDesc(
                keyword, keyword, pageable
        );
    }

    public Page<Perfumer> perfumerSearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return perfumerRepository.findByTitleContainingOrSubTitleContainingOrderByCreatedAtDesc(
                keyword, keyword, pageable
        );
    }

    public Page<BrandStory> brandStorySearch(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return brandStoryRepository.findByTitleContainingOrSubtitleContainingOrderByCreatedAtDesc(
                keyword, keyword, pageable
        );
    }
}
