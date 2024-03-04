package hmoa.hmoaserver.magazine.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.dto.MagazineSaveRequestDto;
import hmoa.hmoaserver.magazine.service.MagazineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "매거진")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/magazine")
public class MagazineController {
    private final MagazineService magazineService;

    @ApiOperation("매거진 저장")
    @PostMapping("/save")
    public ResponseEntity<ResultDto> saveMagazine(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MagazineSaveRequestDto dto) {
        Magazine magazine = magazineService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
