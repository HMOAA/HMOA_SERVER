package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.community.dto.CommunityByCategoryResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentByMemberResponseDto;
import hmoa.hmoaserver.community.service.CommunityCommentLikedMemberService;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.member.dto.*;
import hmoa.hmoaserver.member.service.MemberAddressService;
import hmoa.hmoaserver.member.service.MemberInfoService;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentByMemberResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import hmoa.hmoaserver.perfume.service.PerfumeCommentLikedMemberService;
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

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "멤버")
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PhotoService photoService;
    private final MemberPhotoService memberPhotoService;
    private final CommunityService communityService;
    private final CommunityCommentService communityCommentService;
    private final PerfumeCommentService perfumeCommentService;
    private final PerfumeCommentLikedMemberService perfumeCommentLikedMemberService;
    private final CommunityCommentLikedMemberService commentLikedMemberService;
    private final MemberAddressService memberAddressService;
    private final MemberInfoService memberInfoService;

    @Value("${default.profile}")
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
    public ResponseEntity<MemberResponseDto> findOneMember(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member findMember = memberService.findByMember(token);

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
        Member findMember = memberService.findByMember(token);
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
        Member findMember = memberService.findByMember(token);
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
        Member findMember = memberService.findByMember(token);
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
        Member findMember = memberService.findByMember(token);
        memberService.updateSex(findMember, request.isSex());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "내가 쓴 향수 댓글")
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
    @GetMapping("/perfumeComments")
    public ResponseEntity<List<PerfumeCommentByMemberResponseDto>> findMyPerfumeComments(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        Member member = memberService.findByMember(token);
        Page<PerfumeComment> comments = perfumeCommentService.findPerfumeCommentByMember(member, page);

        List<PerfumeCommentByMemberResponseDto> result = comments.stream().map(comment ->
                new PerfumeCommentByMemberResponseDto(comment, perfumeCommentLikedMemberService.isMemberLikedPerfumeComment(member, comment), true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "내가 쓴 커뮤니티 댓글")
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
    @GetMapping("/communityComments")
    public ResponseEntity<List<CommunityCommentByMemberResponseDto>> findMyCommunityComments(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        Member member = memberService.findByMember(token);
        Page<CommunityComment> comments = communityCommentService.findAllCommunityCommentByMember(member, page);

        List<CommunityCommentByMemberResponseDto> result = comments.stream()
                .map(comment -> new CommunityCommentByMemberResponseDto(comment, commentLikedMemberService.isCommentLikedMember(member, comment), true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "좋아요한 향수 댓글")
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
    @GetMapping("/perfumeHearts")
    public ResponseEntity<List<PerfumeCommentByMemberResponseDto>> findMyPerfumeHearts(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        Member member = memberService.findByMember(token);
        Page<PerfumeCommentLiked> perfumeCommentLikeds = perfumeCommentLikedMemberService.findAllByMember(member, page);

        List<PerfumeCommentByMemberResponseDto> results = perfumeCommentLikeds.stream()
                .map(commentLiked -> new PerfumeCommentByMemberResponseDto(commentLiked.getPerfumeComment(),true, member.isSameMember(commentLiked.getMember())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

    @ApiOperation(value = "좋아요한 커뮤니티 댓글")
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
    @GetMapping("/communityHearts")
    public ResponseEntity<List<CommunityCommentByMemberResponseDto>> findMyCommunityHearts(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        Member member = memberService.findByMember(token);
        Page<CommunityCommentLikedMember> commentLikedMembers = commentLikedMemberService.findAllByMember(member, page);
        List<CommunityCommentByMemberResponseDto> results = commentLikedMembers.stream()
                .map(commentLike -> new CommunityCommentByMemberResponseDto(commentLike.getCommunityComment(), commentLike.getMember().isSameMember(member), commentLike.getCommunityComment().isWrited(member)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

    @ApiOperation(value = "내가 쓴 게시글 조회")
    @GetMapping("/communities")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findAllMyCommunites(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam int page){
        Member member = memberService.findByMember(token);
        Page<Community> communities = communityService.getCommunityByMember(member, page);
        List<CommunityByCategoryResponseDto> result = communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * 프로필 사진 저장
     */
    @ApiOperation(value = "프로필 사진 저장")
    @PostMapping("/profile-photo")
    public ResponseEntity<ResultDto<Object>> saveMemberPhoto(@RequestHeader("X-AUTH-TOKEN") String token, @RequestPart(value = "image") MultipartFile file) {
        Member member = memberService.findByMember(token);

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
        Member member = memberService.findByMember(token);

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
    public ResponseEntity<ResultDto<Object>> deleteMember(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        String code = memberService.delete(member);

        return ResponseEntity.status(200)
                .body(ResultDto.builder().data(code).build());
    }

    /**
     * 주소 저장
     */
    @ApiOperation(value = "배송지 주소 저장")
    @PostMapping("/address")
    public ResponseEntity<ResultDto<Object>> saveMemberAddress(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MemberAddressSaveRequestDto dto) {

        Member member = memberService.findByMember(token);

        memberAddressService.save(dto.toEntity(member));

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    /**
     * 멤버 주문 정보 저장
     */
    @ApiOperation(value = "주문자 정보 저장")
    @PostMapping("/orderInfo")
    public ResponseEntity<ResultDto<Object>> saveOrderInfo(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MemberInfoRequestDto dto) {

        Member member = memberService.findByMember(token);

        memberInfoService.save(dto.toEntity(member));

        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
