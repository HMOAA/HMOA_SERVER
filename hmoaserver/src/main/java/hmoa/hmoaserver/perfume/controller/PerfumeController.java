package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeDefaultResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSaveRequestDto;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import hmoa.hmoaserver.photo.service.PerfumePhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"향수"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/perfume")
public class PerfumeController {

    private final PerfumeService perfumeService;
    private final PhotoService photoService;
    private final PerfumePhotoService perfumePhotoService;
    private final JwtService jwtService;
    private final MemberService memberService;

    @ApiOperation("향수 저장")
    @PostMapping("/new")
    public ResponseEntity<ResultDto<Object>> savePerfume(@RequestParam(value = "image") MultipartFile file, PerfumeSaveRequestDto requestDto) {

        Perfume perfume = perfumeService.save(requestDto);

        photoService.validateFileExistence(file);
        photoService.validateFileType(file);

        perfumePhotoService.savePerfumePhotos(perfume, file);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "향수 단건 조회")
    @GetMapping("/{perfumeId}")
    public ResponseEntity<ResultDto<Object>> findOnePerfume(@PathVariable Long perfumeId) {
        Perfume perfume = perfumeService.findById(perfumeId);

        PerfumeDefaultResponseDto responseDto = new PerfumeDefaultResponseDto(perfume);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation(value = "향수 공감하기")
    @PutMapping("/{perfumeId}/like")
    public ResponseEntity<ResultDto<Object>> savePerfumeLikes(
            @PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        Perfume perfume = perfumeService.findById(perfumeId);

        perfumeService.addPerfumeLikes(perfume, member);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build()
                );
    }

    @ApiOperation(value = "향수 공감 취소하기")
    @DeleteMapping("{perfumeId}/like")
    public ResponseEntity<ResultDto<Object>> deletePerfumeLikes(
            @PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        Perfume perfume = perfumeService.findById(perfumeId);

        perfumeService.deletePerfumeLikes(perfume, member);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build()
                );
    }

}
