package hmoa.hmoaserver.magazine.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.dto.MagazineListResponseDto;
import hmoa.hmoaserver.magazine.dto.MagazineResponseDto;
import hmoa.hmoaserver.magazine.dto.MagazineSaveRequestDto;
import hmoa.hmoaserver.magazine.service.MagazineService;
import hmoa.hmoaserver.member.domain.Member;
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

    @ApiOperation("매거진 프리뷰 저장")
    @PostMapping(value = "/preview/{magazineId}", consumes = "multipart/form-data")
    public ResponseEntity<ResultDto> saveMagazinePreview(@PathVariable Long magazineId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestPart(value="image", required = false) MultipartFile file, @RequestParam("previewContent") String previewContent) {
        Magazine magazine = magazineService.findById(magazineId);

        magazineService.savePreview(magazine, magazinePhotoService.saveMagazinePhoto(magazine, List.of(file)).get(0), previewContent);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("최신 매거진 목록 조회")
    @GetMapping("/list")
    private ResponseEntity<List<MagazineListResponseDto>> findMagazineList(@RequestParam int page) {
        Page<Magazine> magazines = magazineService.findRecentMagazineList(page);

        return ResponseEntity.ok(magazines.stream().map(magazine -> new MagazineListResponseDto(magazine)).collect(Collectors.toList()));
    }

    @ApiOperation("매거진 단건 조회")
    @GetMapping("/{magazineId}")
    public ResponseEntity<MagazineResponseDto> findOneMagazine(@PathVariable Long magazineId) {
        Magazine magazine = magazineService.findById(magazineId);
        magazineService.increaseViewCount(magazine);

        MagazineResponseDto result = new MagazineResponseDto(magazine);

        return ResponseEntity.ok(result);
    }
}
