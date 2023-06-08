package hmoa.hmoaserver.search.controller;

import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSearchResponseDto;
import hmoa.hmoaserver.search.dto.SearchBrandResponseDto;
import hmoa.hmoaserver.search.dto.SearchRequestDto;
import hmoa.hmoaserver.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="검색")
@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @ApiOperation(value = "브랜드 전부 불러오기")
    @GetMapping("/search/brandAll")
    public ResponseEntity<List<BrandDefaultResponseDto>> brandSearchAll(){
        return ResponseEntity.ok(searchService.brandSearchAll());
    }

    @ApiOperation(value = "검색어가 포함된 브랜드 불러오기", notes = "자음은 ㄱ부터 1으로 ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆㅉ순입니다. 예외 경우는 0으로 , 영어 검색시 한글 브랜드 이름 기준으로")
    @PostMapping("/search/brand")
    public ResponseEntity<ResultDto> brandSearch(@RequestBody SearchRequestDto searchRequestDto){
        return ResponseEntity.ok(searchService.brandSearch(searchRequestDto.getSearchWord(),searchRequestDto.getSearchWord(),searchRequestDto.getPage()));
    }

    @ApiOperation(value = "검색어가 포함된 향수 불러오기")
    @PostMapping("/search/perfume")
    public ResponseEntity<List<PerfumeSearchResponseDto>> perfumeSearch(@RequestBody SearchRequestDto searchRequestDto){
        return ResponseEntity.ok(searchService.perfumeSearch(searchRequestDto.getSearchWord(),searchRequestDto.getSearchWord(),searchRequestDto.getPage()));
    }

}
