package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentRequestDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;
import io.swagger.annotations.Api;
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
    @PostMapping("/{perfumeid}/comments")
    public ResponseEntity<ResultDto<Object>> commentSave(@PathVariable Long perfumeid, @RequestBody PerfumeCommentRequestDto dto, @RequestHeader("X-AUTH-TOKEN") String token){
        commentService.commentSave(token,perfumeid,dto);
        log.info("여기");
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("COMMENT_SAVE")
                        .message("댓글 쓰기 완료")
                        .build());
    }
}
