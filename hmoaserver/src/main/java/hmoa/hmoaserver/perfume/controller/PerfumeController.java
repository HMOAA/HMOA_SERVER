package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.service.BrandService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.*;

@Api(tags = {"향수"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/perfume")
@Slf4j
public class PerfumeController {

    private final PerfumeService perfumeService;
    private final BrandService brandService;
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
    public ResponseEntity<ResultDto<Object>> savePerfume(@RequestPart(value = "image") MultipartFile file, PerfumeSaveRequestDto requestDto) {
        if (perfumeService.isExistPerfume(requestDto)) {
            throw new CustomException(null, SERVER_ERROR);
        }

        Perfume perfume = perfumeService.save(requestDto);

        photoService.validateFileExistence(file);
        photoService.validateFileType(file);

        perfumePhotoService.savePerfumePhotos(perfume, file);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("향수 이미지 브랜드 별로 매핑")
    @PostMapping("/save/{brandId}")
    public ResponseEntity<ResultDto<Object>> MappingPerfumeImages(@PathVariable Long brandId) {
        Brand brand = brandService.findById(brandId);
        List<Perfume> perfumes = brand.getPerfumeList();

        try {
            for (Perfume perfume : perfumes) {
                perfumePhotoService.savePerfumePhotoFromS3(perfume);
                log.info("{}", perfume.getKoreanName());
            }
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("향수 저장 새로운 버전")
    @PostMapping("/newList")
    public ResponseEntity<ResultDto<Object>> savePerfumes(@RequestBody List<PerfumeNewRequestDto> dtos) {
        for (PerfumeNewRequestDto dto : dtos) {
            perfumeService.newSave(dto);
        }
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("향수 사진 저장 (New)")
    @PostMapping(value = "/newImage", consumes = "multipart/form-data")
    public ResponseEntity<ResultDto<Object>> savePerfumeImages(@RequestPart(value = "image") List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            log.info("{}", name);
            Perfume perfume = perfumeService.findPerfumeName(removeBrand(name));
            photoService.validateFileExistence(file);
            photoService.validateFileType(file);
            perfumePhotoService.savePerfumePhotos(perfume, file);
        }

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("향수 메인 사진 저장")
    @PostMapping("/{perfumeId}")
    public ResponseEntity<ResultDto<Object>> savePerfumePhoto(@RequestPart(value = "image") MultipartFile file, @PathVariable Long perfumeId) {
        Perfume perfume = perfumeService.findById(perfumeId);

        photoService.validateFileExistence(file);
        photoService.validateFileType(file);

        perfumePhotoService.savePerfumePhotos(perfume, file);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "향수 단건 조회",notes = "sortType 0부터 3까지 0은 노트 모두 null 1~3 은 top heart base 순으로 not null , priceVolume은 Volume 배열 중 몇번째인지 (1부터 시작)\n notes = weather,gender,age 는 int 퍼센트로 리턴 , writed와 liked 는 boolean 으로 true면 내가 썻거나 좋아요 한거 , false 면 반대")
    @GetMapping("/{perfumeId}")
    public ResponseEntity<PerfumeDetailResponseDto> findOnePerfume(@PathVariable Long perfumeId, @RequestHeader(value = "X-AUTH-TOKEN", required = false) String token) {
        Perfume perfume = perfumeService.findById(perfumeId);

        PerfumeDetailResponseDto responseDto = null;
        if (memberService.isTokenNullOrEmpty(token)) {
            responseDto = new PerfumeDetailResponseDto(perfume, false, perfumeReviewService.getReview(perfumeId));

        } else {
            String email = jwtService.getEmail(token);
            Member member = memberService.findByEmail(email);

            boolean memberLikedPerfume = perfumeLikedMemberService.isMemberLikedPerfume(member, perfume);
            responseDto = new PerfumeDetailResponseDto(perfume, memberLikedPerfume, perfumeReviewService.getReview(perfumeId,member));
        }
        return ResponseEntity.ok(responseDto);
    }


    @ApiOperation(value = "향수 공감하기")
    @PutMapping("/{perfumeId}/like")
    public ResponseEntity<ResultDto<Object>> savePerfumeLikes(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token) {
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
                .map(PerfumeDefaultResponseDto::new).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "향수 계절감 평가하기", notes = "weather에 1~4까지 순서대로 봄,여름,가을,겨울로 보내면 됨")
    @PostMapping("/{perfumeId}/weather")
    public ResponseEntity<PerfumeWeatherResponseDto> savePerfumeWeather(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeWeatherRequestDto dto){
        int idx = dto.getWeather();
        if (idx==1||idx==2||idx==3||idx==4){
            return ResponseEntity.ok(perfumeWeatherService.save(token,perfumeId,dto));
        }else throw new CustomException(null,DUPLICATE_WEATHERIDX);
    }

    @ApiOperation(value = "향수 계절감 초기화")
    @DeleteMapping("/{perfumeId}/weather")
    public ResponseEntity<PerfumeWeatherResponseDto> deletePerfumeWeather(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        Perfume perfume = perfumeService.findById(perfumeId);
        return ResponseEntity.ok(perfumeWeatherService.delete(member,perfume));
    }

    @ApiOperation(value = "향수 성별 평가하기", notes = "gender는 1~3까지 순서대로 1은 남자 ,2는 여자 ,3은 중성")
    @PostMapping("/{perfumeId}/gender")
    public ResponseEntity<PerfumeGenderResponseDto> savePerfumeGender(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeGenderRequestDto dto){
        int idx = dto.getGender();
        if (idx==1||idx==2||idx==3){
            return ResponseEntity.ok(perfumeGenderService.save(token,perfumeId,dto));
        }else throw new CustomException(null,DUPLICATE_GENDERIDX);
    }

    @ApiOperation(value = "향수 성별 초기화")
    @DeleteMapping("/{perfumeId}/gender")
    public ResponseEntity<PerfumeGenderResponseDto> deletePerfumeGender(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        Perfume perfume = perfumeService.findById(perfumeId);
        return ResponseEntity.ok(perfumeGenderService.delete(member,perfume));
    }

    @ApiOperation(value = "향수 연령 평가하기", notes = "age는 1~5까지 순서대로 1은 10대 , 2는 20대 ~~ 5는 50대")
    @PostMapping("/{perfumeId}/age")
    public ResponseEntity<PerfumeAgeResponseDto> savePerfumeAge(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeAgeRequestDto dto){
        int idx = dto.getAge();
        if (idx==1||idx==2||idx==3||idx==4||idx==5){
            return ResponseEntity.ok(perfumeAgeService.save(token,perfumeId,dto));
        }else throw new CustomException(null,DUPLICATE_AGEIDX);
    }

    @ApiOperation(value = "향수 연령대 초기화")
    @DeleteMapping("/{perfumeId}/age")
    public ResponseEntity<PerfumeAgeResponseDto> deletePerfumeAge(@PathVariable Long perfumeId, @RequestHeader("X-AUTH-TOKEN") String token){
        Member member = memberService.findByMember(token);
        Perfume perfume = perfumeService.findById(perfumeId);
        return ResponseEntity.ok(perfumeAgeService.deletePerfumeAge(member, perfume));
    }

    @ApiOperation(value = "향수 단건조회 2", notes = "댓글 정보와 같은 브랜드 향수 조회")
    @PostMapping("/{perfumeId}/2")
    public ResponseEntity<PerfumeDetailSecondResponseDto> findOnePerfume2(@PathVariable Long perfumeId, @RequestHeader(name = "X-AUTH-TOKEN",required = false) String token){
        Perfume perfume = perfumeService.findById(perfumeId);
        Page<Perfume> similarPerfumes = perfumeService.findSameBrandPerfume(perfume.getBrand().getId(), perfumeId);
        List<PerfumeSimilarResponseDto> similarDto = similarPerfumes.stream().map(PerfumeSimilarResponseDto::new).collect(Collectors.toList());
        PerfumeDetailSecondResponseDto result = null;
        if(memberService.isTokenNullOrEmpty(token)) {
            result = new PerfumeDetailSecondResponseDto(perfumeCommentService.findTopCommentsByPerfume(perfumeId, 0, 3), similarDto);
            return ResponseEntity.ok(result);
        }
        Member member = memberService.findByMember(token);
        result = new PerfumeDetailSecondResponseDto(perfumeCommentService.findTopCommentsByPerfume(perfumeId,0,3,member),similarDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "향수 출시일자 저장", notes = "향수 출시 일자 저장")
    @PostMapping("/{perfumeId}/relase")
    public ResponseEntity<ResultDto<Object>> saveRelase(@PathVariable Long perfumeId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate relase) {
        Perfume perfume = perfumeService.findById(perfumeId);
        perfumeService.saveRelase(perfume, relase);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "최신 향수 불러오기 (10개)", notes = "최신 향수 불러오기")
    @GetMapping("/recentPerfume")
    public ResponseEntity<List<RecentPerfumeResponseDto>> findRecentPerfume() {
        Page<Perfume> perfumes = perfumeService.findRecentPerfumes();
        List<RecentPerfumeResponseDto> result = perfumes.stream().map(RecentPerfumeResponseDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "제외된 단어가 포함된 향수 제거")
    @PutMapping("/delete/excluded")
    public ResponseEntity<ResultDto<Object>> deleteExcludedPerfumes() {
        perfumeService.deleteExcludedPerfumes();
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    private static String removeBrand(String name) {
        String[] names = name.split("_");
        String productName = names[1].substring(0, names[1].lastIndexOf("."));
        log.info("{}", productName);
        return productName;
    }
}
