package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeLikedMember;
import hmoa.hmoaserver.perfume.dto.PerfumeDefaultResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeGetSecondResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSaveRequestDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeRequestDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderRequestDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherRequestDto;
import hmoa.hmoaserver.perfume.review.service.PerfumeAgeService;
import hmoa.hmoaserver.perfume.review.service.PerfumeGenderService;
import hmoa.hmoaserver.perfume.review.service.PerfumeReviewService;
import hmoa.hmoaserver.perfume.review.service.PerfumeWeatherService;
import hmoa.hmoaserver.perfume.service.PerfumeLikedMemberService;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import hmoa.hmoaserver.photo.service.PerfumePhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.DUPLICATE_LIKED;

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
    private final PerfumeLikedMemberService perfumeLikedMemberService;
    private final PerfumeWeatherService perfumeWeatherService;
    private final PerfumeGenderService perfumeGenderService;
    private final PerfumeAgeService perfumeAgeService;
    private final PerfumeReviewService perfumeReviewService;
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
    @ApiOperation("향수 저장 테스트")
    @PostMapping("/test")
    public ResponseEntity<PerfumeDefaultResponseDto> testPerfume(@RequestBody PerfumeSaveRequestDto dto){
        Perfume perfume = perfumeService.testSave(dto);
        Brand brand = perfume.getBrand();
        PerfumeDefaultResponseDto result = new PerfumeDefaultResponseDto(perfume);
        return ResponseEntity.ok(result);
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

        if (!perfumeLikedMemberService.isMemberLikedPerfume(member, perfume)) {
            perfumeLikedMemberService.save(member, perfume);
            return ResponseEntity.status(200)
                    .body(ResultDto.builder()
                            .build()
                    );
        }
        throw new CustomException(null, DUPLICATE_LIKED);
    }

    @ApiOperation(value = "향수 공감 취소하기")
    @DeleteMapping("/{perfumeId}/like")
    public ResponseEntity<ResultDto<Object>> deletePerfumeLikes(
            @PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        Perfume perfume = perfumeService.findById(perfumeId);

        PerfumeLikedMember perfumeLikedMember = perfumeLikedMemberService.findOneByPerfumeAndMember(perfume, member);
        perfumeLikedMemberService.decrementLikedCountsOfPerfume(perfume);
        perfumeLikedMemberService.delete(perfumeLikedMember);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build()
                );
    }

    @ApiOperation(value = "내가 공감한 향수 목록 조회")
    @GetMapping("/like")
    public ResponseEntity<ResultDto<Object>> findLikedPerfumesByMember(@RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        List<Long> foundPerfumeIds = perfumeLikedMemberService.findLikedPerfumeIdsByMemberId(member.getId());

        List<Perfume> resultPerfumes = new ArrayList<>();
        for (Long perfumeId : foundPerfumeIds) {
            Perfume perfume = perfumeService.findById(perfumeId);
            resultPerfumes.add(perfume);
        }

        List<PerfumeDefaultResponseDto> response = resultPerfumes.stream()
                .map(perfume -> new PerfumeDefaultResponseDto(perfume)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "향수 계절감 평가하기")
    @PostMapping("/{perfumeId}/weather")
    public ResponseEntity<ResultDto<Object>> savePerfumeWeather(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeWeatherRequestDto dto){
        perfumeWeatherService.save(token,perfumeId,dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "향수 성별 평가하기")
    @PostMapping("/{perfumeId}/gender")
    public ResponseEntity<ResultDto<Object>> savePerfumeGender(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeGenderRequestDto dto){
        perfumeGenderService.save(token,perfumeId,dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "향수 연령 평가하기")
    @PostMapping("/{perfumeId}/age")
    public ResponseEntity<ResultDto<Object>> savePerfumeAge(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeAgeRequestDto dto){
        perfumeAgeService.save(token,perfumeId,dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "향수 단건조회 2")
    @PostMapping("/{perfumeId}/2")
    public ResponseEntity<ResultDto<Object>> findOnePerfume2(@PathVariable Long perfumeId){
        List<Double> res = perfumeReviewService.calcurateWeather(perfumeId);
        PerfumeGetSecondResponseDto dto = new PerfumeGetSecondResponseDto(res);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(dto)
                        .build());
    }
}
