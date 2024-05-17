package hmoa.hmoaserver.perfumer.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.perfumer.domain.Perfumer;
import hmoa.hmoaserver.perfumer.dto.PerfumerDefaultResponseDto;
import hmoa.hmoaserver.perfumer.dto.PerfumerDetailResponseDto;
import hmoa.hmoaserver.perfumer.dto.PerfumerSaveRequestDto;
import hmoa.hmoaserver.perfumer.dto.PerfumerUpdateRequestDto;
import hmoa.hmoaserver.perfumer.service.PerfumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"조향사"})
@RestController
@RequestMapping("/perfumer")
@RequiredArgsConstructor
public class PerfumerController {

    private final PerfumerService perfumerService;

    @ApiOperation("조향사 저장")
    @PostMapping("/new")
    public ResponseEntity<ResultDto<Object>> savePerfumer(PerfumerSaveRequestDto requestDto) {
        perfumerService.save(requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("조향사 목록 조회")
    @GetMapping
    public ResponseEntity<PagingDto<Object>> findPerfumer(@RequestParam int pageNum) {
        Page<Perfumer> perfumers = perfumerService.findPerfumer(pageNum);
        boolean isLastPage = PageUtil.isLastPage(perfumers);

        List<PerfumerDefaultResponseDto> responseDto = perfumers.stream()
                .map(PerfumerDefaultResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                        .data(responseDto)
                        .isLastPage(isLastPage)
                        .build());
    }

    @ApiOperation("조향사 목록 조회 (커서 페이징)")
    @GetMapping("/cursor")
    public ResponseEntity<PagingDto<Object>> findPerfumerByCursor(@RequestParam Long cursor) {
        Page<Perfumer> perfumers = perfumerService.findPerfumerByCursor(cursor);
        boolean isLastPage = PageUtil.isLastPage(perfumers);

        List<PerfumerDefaultResponseDto> responseDtos = perfumers.stream()
                .map(PerfumerDefaultResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                        .data(responseDtos)
                        .isLastPage(isLastPage)
                        .build());
    }

    @ApiOperation("조향사 단건 조회")
    @GetMapping("/{perfumerId}")
    public ResponseEntity<ResultDto<Object>> fineOnePerfumer(@PathVariable Long perfumerId) {
        Perfumer perfumer = perfumerService.findById(perfumerId);

        PerfumerDetailResponseDto responseDto = new PerfumerDetailResponseDto(perfumer);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation("조향사 내용 수정")
    @PutMapping("/{perfumerId}/update")
    public ResponseEntity<ResultDto<Object>> updatePerfumerContent(@PathVariable Long perfumerId, PerfumerUpdateRequestDto requestDto) {
        perfumerService.updatePerfumerContent(perfumerId, requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("조향사 삭제")
    @DeleteMapping("/{perfumerId}")
    public ResponseEntity<ResultDto<Object>> deletePerfumer(@PathVariable Long perfumerId) {
        perfumerService.deletePerfumer(perfumerId);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }
}
