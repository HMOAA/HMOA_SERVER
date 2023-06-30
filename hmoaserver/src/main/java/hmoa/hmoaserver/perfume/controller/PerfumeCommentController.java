package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.member.dto.MemberResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentModifyRequestDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentRequestDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"향수댓글"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/perfume")
@Slf4j
public class PerfumeCommentController {
    private final PerfumeCommentService commentService;

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
    @PostMapping("/{perfumeid}/comments")
    public ResponseEntity<ResultDto<Object>> commentSave(@PathVariable Long perfumeid, @RequestBody PerfumeCommentRequestDto dto, @RequestHeader("X-AUTH-TOKEN") String token){
        commentService.commentSave(token,perfumeid,dto);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
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
    public ResponseEntity<ResultDto<Object>> saveHeart(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token){
        commentService.saveHeart(token,commentId);
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
    public ResponseEntity<ResultDto<Object>> deleteHeart(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token){
        commentService.deleteHeart(token,commentId);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "향수 댓글 수정")
    @PutMapping("comments/{commentId}/modify")
    public ResponseEntity<ResultDto<Object>> modifyComment(@PathVariable Long commentId, @RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeCommentModifyRequestDto dto){
        commentService.modifyComment(token,commentId,dto.getContent());
        return ResponseEntity.status(200)
                .body(ResultDto.builder().build());

    }
}
