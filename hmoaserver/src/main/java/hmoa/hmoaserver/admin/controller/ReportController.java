package hmoa.hmoaserver.admin.controller;

import hmoa.hmoaserver.admin.dto.CommunityCommentReportRequestDto;
import hmoa.hmoaserver.admin.dto.CommunityReportRequestDto;
import hmoa.hmoaserver.admin.dto.PerfumeCommentReportRequestDto;
import hmoa.hmoaserver.admin.service.CommunityCommentReportService;
import hmoa.hmoaserver.admin.service.CommunityReportService;
import hmoa.hmoaserver.admin.service.PerfumeCommentReportService;
import hmoa.hmoaserver.common.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"신고하기 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@Slf4j
public class ReportController {
    private final CommunityReportService communityReportService;
    private final CommunityCommentReportService communityCommentReportService;
    private final PerfumeCommentReportService perfumeCommentReportService;

    @ApiOperation("게시글 신고")
    @PostMapping("/community")
    public ResponseEntity<ResultDto> communityReport(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody CommunityReportRequestDto dto){
        communityReportService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
    @ApiOperation("게시판 댓글 신고")
    @PostMapping("/communityComment")
    public ResponseEntity<ResultDto> communityCommentReport(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody CommunityCommentReportRequestDto dto){
        communityCommentReportService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
    @ApiOperation("향수 댓글 신고")
    @PostMapping("/perfumeComment")
    public ResponseEntity<ResultDto> perfumeCommentReport(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody PerfumeCommentReportRequestDto dto){
        perfumeCommentReportService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
