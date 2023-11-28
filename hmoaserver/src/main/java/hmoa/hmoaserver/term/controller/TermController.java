package hmoa.hmoaserver.term.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.term.domain.Term;
import hmoa.hmoaserver.term.dto.TermDefaultResponseDto;
import hmoa.hmoaserver.term.dto.TermDetailResponseDto;
import hmoa.hmoaserver.term.dto.TermSaveRequestDto;
import hmoa.hmoaserver.term.dto.TermUpdateRequestDto;
import hmoa.hmoaserver.term.service.TermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"용어"})
@RestController
@RequestMapping("/term")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @ApiOperation("용어 저장")
    @PostMapping("/new")
    public ResponseEntity<ResultDto<Object>> saveTerm(TermSaveRequestDto requestDto) {
        termService.save(requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("용어 목록 조회")
    @GetMapping
    public ResponseEntity<ResultDto<Object>> findTerm(@RequestParam int pageNum) {
        Page<Term> terms = termService.findTerm(pageNum);

        List<TermDefaultResponseDto> responseDto = terms.stream()
                .map(term -> new TermDefaultResponseDto(term))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation("용어 단건 조회")
    @GetMapping("/{termId}")
    public ResponseEntity<ResultDto<Object>> findOneTerm(@PathVariable Long termId) {
        Term term = termService.findById(termId);

        TermDetailResponseDto responseDto = new TermDetailResponseDto(term);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation("용어 내용 수정")
    @PutMapping("/{termId}/update")
    public ResponseEntity<ResultDto<Object>> updateTermContent(@PathVariable Long termId, TermUpdateRequestDto requestDto) {
        termService.updateTermContent(termId, requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("용어 삭제")
    @DeleteMapping("/{termId}")
    public ResponseEntity<ResultDto<Object>> deleteTerm(@PathVariable Long termId) {
        termService.deleteTerm(termId);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }
}
