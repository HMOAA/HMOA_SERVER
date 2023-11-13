package hmoa.hmoaserver.perfumer.controller;

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
    public ResponseEntity<ResultDto<Object>> findPerfumer() {
        List<Perfumer> perfumers = perfumerService.findPerfumer();

        List<PerfumerDefaultResponseDto> responseDto = perfumers.stream()
                .map(perfumer -> new PerfumerDefaultResponseDto(perfumer))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
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
