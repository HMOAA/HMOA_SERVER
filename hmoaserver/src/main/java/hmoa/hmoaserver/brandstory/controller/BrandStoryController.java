package hmoa.hmoaserver.brandstory.controller;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.brandstory.dto.BrandStoryDefaultResponseDto;
import hmoa.hmoaserver.brandstory.service.BrandStoryService;
import hmoa.hmoaserver.brandstory.domain.BrandStory;
import hmoa.hmoaserver.brandstory.dto.BrandStoryDetailResponseDto;
import hmoa.hmoaserver.brandstory.dto.BrandStorySaveRequestDto;
import hmoa.hmoaserver.brandstory.dto.BrandStoryUpdateRequestDto;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"브랜드 - HPedia"})
@RestController
@RequestMapping("/brandstory")
@RequiredArgsConstructor
public class BrandStoryController {

    private final BrandStoryService brandStoryService;
    private final BrandRepository brandRepository;

    @ApiOperation("브랜드스토리 저장")
    @PostMapping("/new")
    public ResponseEntity<ResultDto<Object>> saveBrandStory(BrandStorySaveRequestDto requestDto) {
        brandStoryService.save(requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("브랜드스토리 목록 조회")
    @GetMapping
    public ResponseEntity<PagingDto<Object>> findBrandStory(@RequestParam int pageNum) {
        Page<BrandStory> brandStories = brandStoryService.findBrandStory(pageNum);
        boolean isLastPage = PageUtil.isLastPage(brandStories);

        List<BrandStoryDefaultResponseDto> responseDtos = brandStories.stream()
                .map(BrandStoryDefaultResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                        .data(responseDtos)
                        .isLastPage(isLastPage)
                        .build());
    }

    @ApiOperation("브랜드스토리 목록 조회 (커서 페이징)")
    @GetMapping("/cursor")
    public ResponseEntity<PagingDto<Object>> findBrandStroyByCursor(@RequestParam Long cursor) {
        Page<BrandStory> brandStories = brandStoryService.findBrandStoryByCursor(cursor);
        boolean isLastPage = PageUtil.isLastPage(brandStories);

        List<BrandStoryDefaultResponseDto> responseDtos = brandStories.stream()
                .map(BrandStoryDefaultResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .data(responseDtos)
                .isLastPage(isLastPage)
                .build());
    }

    @ApiOperation("브랜드스토리 단건 조회")
    @GetMapping("/{brandStoryId}")
    public ResponseEntity<ResultDto<Object>> findOneBrandStory(@PathVariable Long brandStoryId) {
        BrandStory brandStory = brandStoryService.findById(brandStoryId);

        BrandStoryDetailResponseDto responseDto = new BrandStoryDetailResponseDto(brandStory);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation("브랜드스토리 내용 수정")
    @PutMapping("/{brandStoryId}/update")
    public ResponseEntity<ResultDto<Object>> updateBrandStoryContent(@PathVariable Long brandStoryId, BrandStoryUpdateRequestDto requestDto) {
        brandStoryService.updateBrandStoryContent(brandStoryId, requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("브랜드스토리 삭제")
    @DeleteMapping("/{brandStoryId}")
    public ResponseEntity<ResultDto<Object>> deleteBrandStory(@PathVariable Long brandStoryId) {
        brandStoryService.deleteBrandStory(brandStoryId);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("브랜드 내용 없이 저장")
    @PostMapping("/testSave")
    public ResponseEntity<ResultDto<Object>> testSave() {
        List<Brand> brands = brandRepository.findAll();
        brands.forEach(brandStoryService::testSave);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
