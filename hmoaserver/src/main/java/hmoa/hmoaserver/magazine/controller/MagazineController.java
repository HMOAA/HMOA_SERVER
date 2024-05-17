package hmoa.hmoaserver.magazine.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.dto.MagazineListResponseDto;
import hmoa.hmoaserver.magazine.dto.MagazineResponseDto;
import hmoa.hmoaserver.magazine.dto.MagazineSaveRequestDto;
import hmoa.hmoaserver.magazine.dto.TopTastingResponseDto;
import hmoa.hmoaserver.magazine.service.MagazineLikedMemberService;
import hmoa.hmoaserver.magazine.service.MagazineService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.photo.domain.MagazinePhoto;
import hmoa.hmoaserver.photo.service.MagazinePhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "매거진")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/magazine")
public class MagazineController {
    private final MagazineService magazineService;
    private final MagazinePhotoService magazinePhotoService;
    private final MemberService memberService;
    private final MagazineLikedMemberService magazineLikedMemberService;
    private final CommunityService communityService;

    @ApiOperation("매거진 저장")
    @PostMapping("/save")
    public ResponseEntity<ResultDto> saveMagazine(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MagazineSaveRequestDto dto) {
        Magazine magazine = magazineService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("매거진 사진 저장")
    @PostMapping(value = "/saveImage/{magazineId}", consumes = "multipart/form-data")
    public ResponseEntity<ResultDto> saveMagazineImage(@PathVariable Long magazineId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestPart(value = "image") List<MultipartFile> files) {
        Magazine magazine = magazineService.findById(magazineId);

        List<MagazinePhoto> photos = magazinePhotoService.saveMagazinePhoto(magazine, files);
        magazineService.saveImages(magazine, photos);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "매거진 탑 시향기 조회", notes = "시향기 조회 300글자 이후는 ... 으로 잘라서 응답하도록 하였습니다.!")
    @GetMapping("/tastingComment")
    public ResponseEntity<List<TopTastingResponseDto>> getMagazineTastingComment() {
        Page<Community> communities = communityService.getTopCommunitysByCategory(0, Category.시향기);

        return ResponseEntity.ok(communities.stream().map(TopTastingResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation("매거진 프리뷰 저장")
    @PostMapping(value = "/preview/{magazineId}", consumes = "multipart/form-data")
    public ResponseEntity<ResultDto> saveMagazinePreview(@PathVariable Long magazineId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestPart(value="image", required = false) MultipartFile file, @RequestParam("previewContent") String previewContent) {
        Magazine magazine = magazineService.findById(magazineId);

        magazineService.savePreview(magazine, magazinePhotoService.saveMagazinePhoto(magazine, List.of(file)).get(0), previewContent);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("최신 매거진 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<MagazineListResponseDto>> findMagazineList(@RequestParam int page) {
        Page<Magazine> magazines = magazineService.findRecentMagazineList(page);

        return ResponseEntity.ok(magazines.stream().map(MagazineListResponseDto::new).collect(Collectors.toList()));
    }

    @ApiOperation("최신 매거진 목록 조회 (cursor)")
    @GetMapping("/list/cursor")
    public ResponseEntity<PagingDto<Object>> findMagazineList(@RequestParam Long cursor) {
        Page<Magazine> magazines = magazineService.findRecentMagazineListByCursor(cursor);
        boolean isLastPage = PageUtil.isLastPage(magazines);

        List<MagazineListResponseDto> result = magazines.stream().map(MagazineListResponseDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .data(result)
                .isLastPage(isLastPage)
                .build());
    }

    @ApiOperation("매거진 단건 조회")
    @GetMapping("/{magazineId}")
    public ResponseEntity<MagazineResponseDto> findOneMagazine(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @PathVariable Long magazineId) {
        Magazine magazine = magazineService.findById(magazineId);
        magazineService.increaseViewCount(magazine);

        if (memberService.isTokenNullOrEmpty(token)) {
            return ResponseEntity.ok(new MagazineResponseDto(magazine));
        }

        Member member = memberService.findByMember(token);
        boolean isLiked = magazineLikedMemberService.isMagazineLikedMember(magazine,member);

        return ResponseEntity.ok(new MagazineResponseDto(magazine, isLiked));
    }

    @ApiOperation("매거진 좋아요")
    @PutMapping("/{magazineId}/like")
    private ResponseEntity<ResultDto> saveMagazineLike(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long magazineId) {
        Member member = memberService.findByMember(token);
        Magazine magazine = magazineService.findById(magazineId);

        magazineLikedMemberService.save(magazine, member);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("매거진 좋아요 취소")
    @DeleteMapping("/{magazineId}/like")
    private ResponseEntity<ResultDto> deleteMagazineLike(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long magazineId) {
        Member member = memberService.findByMember(token);
        Magazine magazine = magazineService.findById(magazineId);

        magazineLikedMemberService.delete(magazine, member);

        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
