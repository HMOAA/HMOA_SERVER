package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityByCategoryResponseDto;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.member.dto.*;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;
import hmoa.hmoaserver.photo.service.MemberPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "멤버")
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final JwtService jwtService;
    private final MemberService memberService;
    private final PhotoService photoService;
    private final MemberPhotoService memberPhotoService;
    private final PerfumeCommentService perfumeCommentService;

    @Value("${defalut.profile}")
    private String DEFALUT_PROFILE_URL;


    /**
     * 멤버 단건 조회
     */
    @ApiOperation(value = "멤버 단건 조회")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = MemberResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping()
    public ResponseEntity<MemberResponseDto> findOneMember(HttpServletRequest request, @RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        if (findMember.getRole() == Role.GUEST) {
            throw new CustomException(null, Code.MEMBER_NOT_FOUND);
        }
        MemberResponseDto resultDto = new MemberResponseDto(findMember);
        if (findMember.getMemberPhoto() == null) {
            resultDto.setMemberImageUrl(DEFALUT_PROFILE_URL);
        }
        return ResponseEntity.ok(resultDto);

    }

    /**
     * 회원 가입시 정보 수정
     */
    @ApiOperation(value = "회원 가입")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = MemberResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/join")
    public ResponseEntity<MemberResponseDto> joinMember(@RequestBody JoinUpdateRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.joinMember(findMember, request.getAge(), request.isSex(), request.getNickname());
        MemberResponseDto reslutDto = new MemberResponseDto(findMember);
        return ResponseEntity.ok(reslutDto);
    }

    /**
     * 닉네임 변경
     */
    @ApiOperation(value = "닉네임 변경")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "이미 존재하는 닉네임입니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/nickname")
    public ResponseEntity<ResultDto<Object>> updateNickname(@RequestBody NicknameRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.updateNickname(findMember, request.getNickname());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    /**
     * 닉네임 중복 검사
     */
    @ApiOperation(value = "닉네임 중복 검사")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "인증 실패",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "이미 존재하는 닉네임입니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PostMapping("/existsnickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody NicknameRequestDto request) {
        return ResponseEntity.ok(memberService.isExistingNickname(request.getNickname()));
    }

    /**
     * 나이 업데이트
     */
    @ApiOperation(value = "나이 업데이트")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "인증 실패",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/age")
    public ResponseEntity<ResultDto<Object>> updateAge(@RequestBody AgeRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.updateAge(findMember, request.getAge());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    /**
     * 성별 업데이트
     */
    @ApiOperation(value = "성별 업데이트")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답"
            ),
            @ApiResponse(
                    code = 401,
                    message = "인증 실패",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PatchMapping("/sex")
    public ResponseEntity<ResultDto<Object>> updateSex(@RequestBody SexRequestDto request, @RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.updateSex(findMember, request.isSex());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "내가 쓴 댓글")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = PerfumeCommentResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping("/comments")
    public ResponseEntity<List<PerfumeCommentResponseDto>> findMyComments(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<PerfumeComment> comments = memberService.findByComment(token, page);
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        List<PerfumeCommentResponseDto> result = new ArrayList<>();
        for (PerfumeComment pc : comments) {
            log.info("{}", pc.getId());
            PerfumeCommentResponseDto dto = new PerfumeCommentResponseDto(pc,false,member,DEFALUT_PROFILE_URL);
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "좋아요한 댓글")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = PerfumeCommentResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping("/hearts")
    public ResponseEntity<List<PerfumeCommentResponseDto>> findMyHearts(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        Page<PerfumeComment> comments = memberService.findByHeartComment(token, page);
        List<PerfumeCommentResponseDto> results = comments.stream()
                .map(comment -> new PerfumeCommentResponseDto(comment,true,member,DEFALUT_PROFILE_URL)).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    /**
     * 프로필 사진 저장
     */
    @ApiOperation(value = "프로필 사진 저장")
    @PostMapping("/profile-photo")
    public ResponseEntity<ResultDto<Object>> saveMemberPhoto(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "image") MultipartFile file) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        photoService.validateFileExistence(file);
        photoService.validateFileExistence(file);

        memberService.saveMemberPhoto(member, file);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    /**
     * 프로필 사진 삭제
     */
    @ApiOperation(value = "프로필 사진 삭제")
    @DeleteMapping("/profile-photo")
    public ResponseEntity<ResultDto<Object>> deleteMemberPhoto(@RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        memberPhotoService.validateMemberPhotoIsExistence(member);
        memberPhotoService.delete(member.getMemberPhoto());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    /**
     * 회원 탈퇴
     */
    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<ResultDto<Object>> deleteMember(@RequestHeader("X-AUTH-TOKEN") String token){
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);
        perfumeCommentService.deleteMemberComment(member);
        String code = memberService.delete(member);
        return ResponseEntity.status(200)
                .body(ResultDto.builder().data(code).build());
    }

    @ApiOperation(value = "내가 쓴 게시글 조회")
    @GetMapping("/communities")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findAllMyCommunites(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam int page){
        Member member = memberService.findByMember(token);
        Page<Community> communities = memberService.findByMyCommunities(member, page);
        List<CommunityByCategoryResponseDto> result = communities.stream().map(community -> new CommunityByCategoryResponseDto(community)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
