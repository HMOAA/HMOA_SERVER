package hmoa.hmoaserver.brandstory.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brandstory.domain.BrandStory;
import hmoa.hmoaserver.brandstory.dto.BrandStorySaveRequestDto;
import hmoa.hmoaserver.brandstory.dto.BrandStoryUpdateRequestDto;
import hmoa.hmoaserver.brandstory.repository.BrandStoryRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hmoa.hmoaserver.exception.Code.BRANDSTORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandStoryService {

    private final BrandStoryRepository brandStoryRepository;

    public BrandStory save(BrandStorySaveRequestDto requestDto) {
        return brandStoryRepository.save(requestDto.toEntity());
    }

    public Page<BrandStory> findBrandStory(int pageNum) {
        return brandStoryRepository.findAll(PageRequest.of(pageNum, 15));
    }

    public BrandStory findById(Long brandStoryId) {
        BrandStory brandStory = brandStoryRepository.findById(brandStoryId)
                .orElseThrow(() -> new CustomException(null, BRANDSTORY_NOT_FOUND));

        if (brandStory.isDeleted() == true) {
            throw new CustomException(null, BRANDSTORY_NOT_FOUND);
        }

        return brandStory;
    }

    public void updateBrandStoryContent(Long brandStoryId, BrandStoryUpdateRequestDto requestDto) {
        BrandStory foundBrandStory = findById(brandStoryId);
        foundBrandStory.updateContent(requestDto.getContent());
        brandStoryRepository.save(foundBrandStory);
    }

    public void deleteBrandStory(Long brandStoryId) {
        BrandStory brandStory = findById(brandStoryId);

        brandStory.delete();
        brandStoryRepository.save(brandStory);
    }

    public void testSave(Brand brand) {
        brandStoryRepository.save(BrandStory.builder()
                .title(brand.getBrandName())
                .subtitle(brand.getEnglishName())
                .content(null)
                .build());
    }
}
