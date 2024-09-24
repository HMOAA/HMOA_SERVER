package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.dto.CommunityByCategoryResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentByMemberResponseDto;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.member.MemberFacade;
import hmoa.hmoaserver.member.dto.*;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentByMemberResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "멤버")
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberFacade memberFacade;

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
        return ResponseEntity.ok(memberFacade.getOneMember(token));
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
        return ResponseEntity.ok(memberFacade.joinMember(token, request));
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
        memberFacade.updateNickname(token, request);
        return ResponseEntity.ok(ResultDto.builder().build());
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
        return ResponseEntity.ok(memberFacade.checkNicknameDuplicate(request));
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
        memberFacade.updateAge(token, request);
        return ResponseEntity.ok(ResultDto.builder().build());
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
        memberFacade.updateSex(token, request);
        return ResponseEntity.ok(ResultDto.builder().build());
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
        return ResponseEntity.ok(memberFacade.getMyPerfumeComments(token, page));
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
        return ResponseEntity.ok(memberFacade.getMyCommunityComments(token, page));
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
        return ResponseEntity.ok(memberFacade.getMyPerfumeCommentsByHearts(token, page));
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
        return ResponseEntity.ok(memberFacade.getMyCommunityComentsByHearts(token, page));
    }

    @ApiOperation(value = "내가 쓴 게시글 조회")
    @GetMapping("/communities")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findAllMyCommunites(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam int page){
        return ResponseEntity.ok(memberFacade.getMyCommunities(token, page));
    }

    /**
     * 프로필 사진 저장
     */
    @ApiOperation(value = "프로필 사진 저장")
    @PostMapping("/profile-photo")
    public ResponseEntity<ResultDto<Object>> saveMemberPhoto(@RequestHeader("X-AUTH-TOKEN") String token, @RequestPart(value = "image") MultipartFile file) {
        memberFacade.saveMemberPhoto(token, file);
        return ResponseEntity.status(200).body(ResultDto.builder().build());
    }

    /**
     * 프로필 사진 삭제
     */
    @ApiOperation(value = "프로필 사진 삭제")
    @DeleteMapping("/profile-photo")
    public ResponseEntity<ResultDto<Object>> deleteMemberPhoto(@RequestHeader("X-AUTH-TOKEN") String token) {
        memberFacade.deleteMemberPhoto(token);
        return ResponseEntity.status(200).body(ResultDto.builder().build());
    }

    /**
     * 회원 탈퇴
     */
    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<ResultDto<Object>> deleteMember(@RequestHeader("X-AUTH-TOKEN") String token) {
        memberFacade.deleteMember(token);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    /**
     * 주소 저장
     */
    @ApiOperation(value = "배송지 주소 저장")
    @PostMapping("/address")
    public ResponseEntity<ResultDto<Object>> saveMemberAddress(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MemberAddressSaveRequestDto dto) {
        memberFacade.saveMemberAddress(token, dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    /**
     * 멤버 주문 정보 저장
     */
    @ApiOperation(value = "주문자 정보 저장")
    @PostMapping("/orderInfo")
    public ResponseEntity<ResultDto<Object>> saveOrderInfo(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MemberInfoRequestDto dto) {
        memberFacade.saveOrderInfo(token, dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    /**
     * 주문자 정보 조회
     */
    @ApiOperation(value = "주문자 정보 조회")
    @GetMapping("/orderInfo")
    public ResponseEntity<MemberInfoResponseDto> getOrderInfo(@RequestHeader("X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(memberFacade.getOrderInfo(token));
    }

    /**
     * 배송지 정보 조회
     */
    @ApiOperation(value = "배송지 정보 조회")
    @GetMapping("/address")
    public ResponseEntity<MemberAddressResponseDto> getAddress(@RequestHeader("X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(memberFacade.getAddress(token));
    }

    @ApiOperation(value = "주문 내역 조회")
    @GetMapping("/order")
    public ResponseEntity<List<MemberOrderResponseDto>> getOrder(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam(value = "page", defaultValue = "0") int page) {
        return ResponseEntity.ok(memberFacade.getMemberOrders(token, page));
    }
}
