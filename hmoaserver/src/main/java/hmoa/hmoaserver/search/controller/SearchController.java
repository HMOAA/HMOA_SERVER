package hmoa.hmoaserver.search.controller;

import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.search.dto.PerfumeNameSearchResponseDto;
import hmoa.hmoaserver.search.dto.PerfumeSearchResponseDto;
import hmoa.hmoaserver.search.dto.BrandSearchResponseDto;
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
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @ApiOperation(value = "브랜드 전부 불러오기")
    @GetMapping("/brandAll")
    public ResponseEntity<List<BrandDefaultResponseDto>> brandSearchAll(@RequestParam int consonant){
        return ResponseEntity.ok(searchService.brandSearchAll());
    }

    @ApiOperation(value = "검색어가 포함된 브랜드 불러오기", notes = "자음은 ㄱ부터 1으로 ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆㅉ순입니다. 예외 경우는 0으로 , 영어 검색시 한글 브랜드 이름 기준으로")
    @GetMapping("/brand")
    public ResponseEntity<List<BrandSearchResponseDto>> brandSearch(@RequestParam String searchWord){
        return ResponseEntity.ok(searchService.brandSearch(searchWord));
    }

    @ApiOperation(value = "검색어가 포함된 향수 불러오기 (향수 정보)", notes = "향수 정보가 포함된 내용을 리턴")
    @GetMapping("/perfume")
    public ResponseEntity<List<PerfumeSearchResponseDto>> perfumeSearch(@RequestParam int page, @RequestParam String searchWord){
        return ResponseEntity.ok(searchService.perfumeSearch(searchWord,searchWord,page));
    }

    @ApiOperation(value = "검색어가 포함된 향수 불러오기 (Name만)", notes = "향수 이름만 String 배열로 리턴")
    @GetMapping("/perfumeName")
    public ResponseEntity<List<PerfumeNameSearchResponseDto>> perfumeStringSearch(@RequestParam int page, @RequestParam String searchWord){
        return ResponseEntity.ok(searchService.perfumeNameSearch(searchWord,page));
    }
}
