package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.dto.CommunityCommentByMemberResponseDto;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import hmoa.hmoaserver.perfume.dto.*;
import hmoa.hmoaserver.perfume.service.PerfumeCommentLikedMemberService;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"향수댓글"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/perfume")
@Slf4j
public class PerfumeCommentController {
    private final PerfumeCommentService commentService;
    private final PerfumeCommentLikedMemberService commentLikedMemberService;
    private final MemberService memberService;

    @ApiOperation(value = "향수 댓글 저장")
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
                    message = "일치하는 회원 또는 향수가 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PostMapping("/{perfumeId}/comments")
    public ResponseEntity<PerfumeCommentResponseDto> commentSave(@PathVariable Long perfumeId, @RequestBody PerfumeCommentRequestDto dto, @RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        PerfumeComment perfumeComment = commentService.commentSave(member, perfumeId, dto);
        return ResponseEntity.ok(new PerfumeCommentResponseDto(perfumeComment, false, member));
    }

    @ApiOperation(value = "향수 댓글 단 건 조회")
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<PerfumeCommentResponseDto> findOnePerfumeComment(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @PathVariable Long commentId) {
        PerfumeComment comment = commentService.findOnePerfumeComment(commentId);
        if (memberService.isTokenNullOrEmpty(token)) {
            return ResponseEntity.ok(new PerfumeCommentResponseDto(comment));
        }
        Member member = memberService.findByMember(token);
        return ResponseEntity.ok(new PerfumeCommentResponseDto(comment, commentService.isPerfumeCommentLiked(comment, member), member));
    }

    @ApiOperation(value = "한 향수에 달린 댓글 전부 불러오기(최신순)")
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
                    message = "일치하는 회원 또는 향수가 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping("/{perfumeId}/comments")
    public ResponseEntity<PerfumeCommentGetResponseDto> findCommentsByPerfume(@PathVariable Long perfumeId, @RequestParam int page, @RequestHeader(name = "X-AUTH-TOKEN",required = false) String token) {
        if (memberService.isTokenNullOrEmpty(token)) {
            PerfumeCommentGetResponseDto result = commentService.findCommentsByPerfume(perfumeId, page);
            return ResponseEntity.ok(result);
        }
        Member member = memberService.findByMember(token);
        PerfumeCommentGetResponseDto result = commentService.findCommentsByPerfume(perfumeId, page, member);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "한 향수에 달린 댓글 전부 불러오기(최신순 , 커서 페이징)", notes = "처음 Cursor는 0으로 보내기, 다음 Cursor는 마지막 Comment의 id 값 보내기.")
    @GetMapping("/{perfumeId}/comments/cursor")
    public ResponseEntity<PerfumeCommentGetResponseDto> findCommentsByPerfume(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @PathVariable Long perfumeId, @RequestParam Long cursor) {
        Page<PerfumeComment> comments = commentService.findCommentsByPerfume(perfumeId, cursor);
        boolean isLastPage = isLastPage(comments);
        Long count = commentService.totalCountsByPerfume(perfumeId);

        if (memberService.isTokenNullOrEmpty(token)) {
            List<PerfumeCommentResponseDto> dtos = comments.stream().map(PerfumeCommentResponseDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(new PerfumeCommentGetResponseDto(count, isLastPage, dtos));
        }

        Member member = memberService.findByMember(token);
        List<PerfumeCommentResponseDto> dtos = comments.stream().map(comment ->
                new PerfumeCommentResponseDto(comment, commentService.isPerfumeCommentLiked(comment, member), member))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PerfumeCommentGetResponseDto(count, isLastPage, dtos));
    }

    @ApiOperation(value = "한 향수에 달린 댓글 전부 불러오기(좋아요순)")
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
                    message = "일치하는 회원 또는 향수가 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping("/{perfumeId}/comments/top")
    public ResponseEntity<PerfumeCommentGetResponseDto> findTopCommentsByPerfume(@PathVariable Long perfumeId, @RequestParam int page, @RequestHeader(name = "X-AUTH-TOKEN",required = false) String token) {
        if (memberService.isTokenNullOrEmpty(token)) {
            PerfumeCommentGetResponseDto result = commentService.findTopCommentsByPerfume(perfumeId, page,10);
            return ResponseEntity.ok(result);
        }
        Member member = memberService.findByMember(token);
        PerfumeCommentGetResponseDto result = commentService.findTopCommentsByPerfume(perfumeId, page,10, member);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "향수 댓글 하트")
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
                    message = "일치하는 회원또는 댓글이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "하트는 한 번만 가능합니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @PutMapping("comments/{commentId}/like")
    public ResponseEntity<ResultDto<Object>> saveHeart(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token) {
        commentService.saveLike(token,commentId);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "향수 댓글 하트지우기")
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
                    message = "일치하는 회원 또는 댓글 또는 하트가 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "하트는 한 번만 가능합니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @DeleteMapping("comments/{commentId}/like")
    public ResponseEntity<ResultDto<Object>> deleteHeart(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token) {
        commentService.deleteLike(token, commentId);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "내가 쓴 향수 댓글 조회 (커서 페이징)")
    @GetMapping("comments/me")
    public ResponseEntity<PagingDto<Object>> findAllByMember(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if (cursor == 0) cursor = (long) PageSize.DEFAULT_CURSOR.getSize();
        Page<PerfumeComment> comments = commentService.findPerfumeCommentByMemberAndCursor(member, cursor);
        boolean isLastPage = PageUtil.isLastPage(comments);

        List<PerfumeCommentByMemberResponseDto> result = comments.stream().map(comment ->
                new PerfumeCommentByMemberResponseDto(comment, commentLikedMemberService.isMemberLikedPerfumeComment(member, comment), true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(result)
                .build());
    }

    @ApiOperation(value = "내가 좋아요한 향수 댓글 조회 (커서 페이징)")
    @GetMapping("comments/like")
    public ResponseEntity<PagingDto<Object>> findAllByLiked(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if (cursor == 0) cursor = (long) PageSize.DEFAULT_CURSOR.getSize();
        Page<PerfumeCommentLiked> commentLikeds = commentLikedMemberService.findAllByMemberAndCursor(member, cursor);
        boolean isLastPage = PageUtil.isLastPage(commentLikeds);

        List<PerfumeCommentByMemberResponseDto> result = commentLikeds.stream().map(commentLiked ->
                new PerfumeCommentByMemberResponseDto(commentLiked.getPerfumeComment(), true, member.isSameMember(commentLiked.getPerfumeComment().getMember())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(result)
                .build());
    }

    @ApiOperation(value = "향수 댓글 수정")
    @PutMapping("comments/{commentId}/modify")
    public ResponseEntity<PerfumeCommentResponseDto> modifyComment(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeCommentModifyRequestDto dto) {
        Member member = memberService.findByMember(token);
        PerfumeComment comment = commentService.modifyComment(token,commentId,dto.getContent());
        return ResponseEntity.ok(new PerfumeCommentResponseDto(comment, true, member));

    }

    @ApiOperation(value = "향수 댓글 삭제")
    @DeleteMapping("/comments/{commentId}/delete")
    public ResponseEntity<ResultDto<Object>> deleteComment(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        commentService.deleteComment(member, commentId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    private static boolean isLastPage(Page<PerfumeComment> comments) {
        return !comments.hasNext();
    }
}
