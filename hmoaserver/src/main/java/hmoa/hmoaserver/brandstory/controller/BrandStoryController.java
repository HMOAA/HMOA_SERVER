package hmoa.hmoaserver.brandstory.controller;

import hmoa.hmoaserver.brandstory.dto.BrandStoryDefaultResponseDto;
import hmoa.hmoaserver.brandstory.service.BrandStoryService;
import hmoa.hmoaserver.brandstory.domain.BrandStory;
import hmoa.hmoaserver.brandstory.dto.BrandStoryDetailResponseDto;
import hmoa.hmoaserver.brandstory.dto.BrandStorySaveRequestDto;
import hmoa.hmoaserver.brandstory.dto.BrandStoryUpdateRequestDto;
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
    public ResponseEntity<ResultDto<Object>> findBrandStory(@RequestParam int pageNum) {
        Page<BrandStory> brandStories = brandStoryService.findBrandStory(pageNum);

        List<BrandStoryDefaultResponseDto> responseDto = brandStories.stream()
                .map(brandStory -> new BrandStoryDefaultResponseDto(brandStory))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
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
}
