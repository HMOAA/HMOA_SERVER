package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeLikedMember;
import hmoa.hmoaserver.perfume.dto.*;
import hmoa.hmoaserver.perfume.review.dto.*;
import hmoa.hmoaserver.perfume.review.service.PerfumeAgeService;
import hmoa.hmoaserver.perfume.review.service.PerfumeGenderService;
import hmoa.hmoaserver.perfume.review.service.PerfumeReviewService;
import hmoa.hmoaserver.perfume.review.service.PerfumeWeatherService;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;
import hmoa.hmoaserver.perfume.service.PerfumeLikedMemberService;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import hmoa.hmoaserver.photo.service.PerfumePhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
@Slf4j
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
    private final PerfumeCommentService perfumeCommentService;

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
    public ResponseEntity<PerfumeDetailResponseDto> testPerfume(@RequestBody PerfumeSaveRequestDto dto){
        Perfume perfume = perfumeService.testSave(dto);
        Brand brand = perfume.getBrand();
        PerfumeDetailResponseDto result = new PerfumeDetailResponseDto(perfume, false);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "향수 단건 조회",notes = "sortType 0은 노트 3개 구분, 1은 singleNotes로 String 배열 , priceVolume은 Volume 배열 중 몇번째인지 (1부터 시작)")
    @GetMapping("/{perfumeId}")
    public ResponseEntity<ResultDto<Object>> findOnePerfume(@PathVariable Long perfumeId, @RequestHeader(value = "X-AUTH-TOKEN", required = false) String token) {
        Perfume perfume = perfumeService.findById(perfumeId);

        if (token == null) {
            PerfumeDetailResponseDto responseDto = new PerfumeDetailResponseDto(perfume, false);

            return ResponseEntity.status(200)
                    .body(ResultDto.builder()
                            .data(responseDto)
                            .build());
        } else {
            String email = jwtService.getEmail(token);
            Member member = memberService.findByEmail(email);

            boolean memberLikedPerfume = perfumeLikedMemberService.isMemberLikedPerfume(member, perfume);
            PerfumeDetailResponseDto responseDto = new PerfumeDetailResponseDto(perfume, memberLikedPerfume);

            return ResponseEntity.status(200)
                    .body(ResultDto.builder()
                            .data(responseDto)
                            .build());
        }

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

    @ApiOperation(value = "향수 계절감 평가하기", notes = "weather에 1~4까지 순서대로 봄,여름,가을,겨울로 보내면 됨")
    @PostMapping("/{perfumeId}/weather")
    public ResponseEntity<PerfumeWeatherResponseDto> savePerfumeWeather(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeWeatherRequestDto dto){
        return ResponseEntity.ok(perfumeWeatherService.save(token,perfumeId,dto));
    }

    @ApiOperation(value = "향수 성별 평가하기", notes = "gender는 1~3까지 순서대로 1은 남자 ,2는 여자 ,3은 중성")
    @PostMapping("/{perfumeId}/gender")
    public ResponseEntity<PerfumeGenderResponseDto> savePerfumeGender(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeGenderRequestDto dto){
        return ResponseEntity.ok(perfumeGenderService.save(token,perfumeId,dto));
    }

    @ApiOperation(value = "향수 연령 평가하기", notes = "age는 1~5까지 순서대로 1은 10대 , 2는 20대 ~~ 5는 50대")
    @PostMapping("/{perfumeId}/age")
    public ResponseEntity<PerfumeAgeResponseDto> savePerfumeAge(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeAgeRequestDto dto){
        return ResponseEntity.ok(perfumeAgeService.save(token,perfumeId,dto));
    }

    @ApiOperation(value = "향수 단건조회 2",notes = "weather,gender,age 는 int 퍼센트로 리턴 , writed와 liked 는 boolean 으로 true면 내가 썻거나 좋아요 한거 , false 면 반대")
    @PostMapping("/{perfumeId}/2")
    public ResponseEntity<PerfumeGetSecondResponseDto> findOnePerfume2(@PathVariable Long perfumeId,@RequestHeader(name = "X-AUTH-TOKEN",required = false) String token){
        log.info("{}",token);
        Perfume perfume = perfumeService.findById(perfumeId);
        Page<Perfume> perfumes = perfumeService.findTopPerfumesByBrand(perfume.getBrand().getId(),0);
        List<PerfumeSimilarResponseDto> similarResponseDtos= perfumes.stream().map(findPerfume -> new PerfumeSimilarResponseDto(findPerfume)).collect(Collectors.toList());
        if(token==null || token.equals("")) {
            PerfumeGetSecondResponseDto resultDto = perfumeReviewService.getReview(perfumeId);
            PerfumeCommentGetResponseDto commentDto = perfumeCommentService.findTopCommentsByPerfume(perfumeId, 0, 3);
            resultDto.setCommentInfo(commentDto);
            resultDto.setSimilarPerfumes(similarResponseDtos);
            return ResponseEntity.ok(resultDto);
        }else {
            Member member = memberService.findByMember(token);
            PerfumeGetSecondResponseDto resultDto = perfumeReviewService.getReview(perfumeId,token);
            PerfumeCommentGetResponseDto commentDto = perfumeCommentService.findTopCommentsByPerfume(perfumeId, 0, 3,member);
            resultDto.setCommentInfo(commentDto);
            resultDto.setSimilarPerfumes(similarResponseDtos);
            return ResponseEntity.ok(resultDto);
        }

    }
}
