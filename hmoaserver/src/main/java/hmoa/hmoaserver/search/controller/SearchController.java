package hmoa.hmoaserver.search.controller;

import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.brandstory.domain.BrandStory;
import hmoa.hmoaserver.brandstory.dto.BrandStoryDefaultResponseDto;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityByCategoryResponseDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.dto.NoteDefaultResponseDto;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.service.PerfumeLikedMemberService;
import hmoa.hmoaserver.perfumer.domain.Perfumer;
import hmoa.hmoaserver.perfumer.dto.PerfumerDefaultResponseDto;
import hmoa.hmoaserver.search.dto.PerfumeNameSearchResponseDto;
import hmoa.hmoaserver.search.dto.PerfumeSearchResponseDto;
import hmoa.hmoaserver.search.dto.BrandSearchResponseDto;
import hmoa.hmoaserver.search.service.SearchService;
import hmoa.hmoaserver.term.domain.Term;
import hmoa.hmoaserver.term.dto.TermDefaultResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags="검색")
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final MemberService memberService;
    private final PerfumeLikedMemberService perfumeLikedMemberService;

    @ApiOperation(value = "브랜드 전부 불러오기",notes = "consonant ㄱ부터 1로 ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆㅉ 순 입니다.")
    @GetMapping("/brandAll")
    public ResponseEntity<List<BrandDefaultResponseDto>> brandSearchAll(@RequestParam int consonant) {
        return ResponseEntity.ok(searchService.brandSearchAll(consonant));
    }

    @ApiOperation(value = "검색어가 포함된 브랜드 불러오기", notes = "자음은 ㄱ부터 1으로 ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆㅉ순입니다. 예외 경우는 0으로 , 영어 검색시 한글 브랜드 이름 기준으로")
    @GetMapping("/brand")
    public ResponseEntity<List<BrandSearchResponseDto>> brandSearch(@RequestParam String searchWord) {
        return ResponseEntity.ok(searchService.brandSearch(searchWord));
    }

    @ApiOperation(value = "검색어가 포함된 향수 불러오기 (향수 정보)", notes = "향수 정보가 포함된 내용을 리턴")
    @GetMapping("/perfume")
    public ResponseEntity<List<PerfumeSearchResponseDto>> perfumeSearch(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @RequestParam int page, @RequestParam String searchWord) {
        Page<Perfume> perfumePage = searchService.perfumeSearch(searchWord, searchWord, page);

        if (memberService.isTokenNullOrEmpty(token)) {
            List<PerfumeSearchResponseDto> perfumes = perfumePage.stream().map(perfume -> new PerfumeSearchResponseDto(perfume, false)).collect(Collectors.toList());
            return ResponseEntity.ok(perfumes);
        }

        Member member = memberService.findByMember(token);
        List<PerfumeSearchResponseDto> perfumes = perfumePage.stream().map(perfume -> {
            boolean isLiked = perfumeLikedMemberService.isMemberLikedPerfume(member, perfume);
            log.info("{}", isLiked);
            return new PerfumeSearchResponseDto(perfume, isLiked);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(perfumes);
    }

    @ApiOperation(value = "검색어가 포함된 향수 불러오기 (Name만)", notes = "향수 이름만 String 배열로 리턴")
    @GetMapping("/perfumeName")
    public ResponseEntity<List<PerfumeNameSearchResponseDto>> perfumeStringSearch(@RequestParam int page, @RequestParam String searchWord) {
        return ResponseEntity.ok(searchService.perfumeNameSearch(searchWord,page));
    }

    @ApiOperation(value = "검색어가 포함된 게시글 불러오기 (카테고리별)", notes = "게시글 검색")
    @GetMapping("/community/category")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> communitySearchForCategory(@RequestParam Category category, @RequestParam int page, @RequestParam String seachWord) {
        Page<Community> communities = searchService.communitySearchForCategory(category, seachWord, page);
        return ResponseEntity.ok(communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation(value = "검색어가 포함된 게시글 불러오기 (전체)", notes = "게시글 검색")
    @GetMapping("/community")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> communitySearch(@RequestParam int page, @RequestParam String seachWord) {
        Page<Community> communities = searchService.communitySearch(seachWord, page);
        return ResponseEntity.ok(communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation(value = "검색어가 포함된 노트 불러오기", notes = "노트 검색")
    @GetMapping("/note")
    public ResponseEntity<List<NoteDefaultResponseDto>> noteSearch(@RequestParam int page, @RequestParam String seachWord) {
        Page<Note> notes = searchService.noteSearch(seachWord, page);
        return ResponseEntity.ok(notes.stream().map(NoteDefaultResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation(value = "검색어가 포함된 용어 불러오기", notes = "용어 검색")
    @GetMapping("/term")
    public ResponseEntity<List<TermDefaultResponseDto>> termSearch(@RequestParam int page, @RequestParam String seachWord) {
        Page<Term> terms = searchService.termSearch(seachWord, page);
        return ResponseEntity.ok(terms.stream().map(TermDefaultResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation(value = "검색어가 포함된 조향사 불러오기", notes = "조향사 검색")
    @GetMapping("/perfumer")
    public ResponseEntity<List<PerfumerDefaultResponseDto>> perfumerSearch(@RequestParam int page, @RequestParam String seachWord) {
        Page<Perfumer> perfumers = searchService.perfumerSearch(seachWord, page);
        return ResponseEntity.ok(perfumers.stream().map(PerfumerDefaultResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation(value = "검색어가 포함된 브랜드(H-Pedia) 불러오기", notes = "브랜드 검색")
    @GetMapping("/brandStory")
    public ResponseEntity<List<BrandStoryDefaultResponseDto>> brandStorySearch(@RequestParam int page, @RequestParam String seachWord) {
        Page<BrandStory> brandStorys = searchService.brandStorySearch(seachWord, page);
        return ResponseEntity.ok(brandStorys.stream().map(BrandStoryDefaultResponseDto::new).collect(Collectors.toList()));
    }
}
