package hmoa.hmoaserver.homemenu.controller;

import hmoa.hmoaserver.homemenu.HomeMenuFacade;
import hmoa.hmoaserver.homemenu.dto.HomeMenuAllResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuDefaultResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuFirstResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"메인 페이지"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Slf4j
public class MainPageController {

    private final HomeMenuFacade homeMenuFacade;

    @ApiOperation("메인 페이지 첫 번째")
    @GetMapping("/first")
    public ResponseEntity<HomeMenuFirstResponseDto> getHomePerfumeList() {
        return ResponseEntity.ok(homeMenuFacade.getHomeMenuFirst());
    }

    @ApiOperation("메인 페이지 두 번째")
    @GetMapping("/second")
    public ResponseEntity<List<HomeMenuDefaultResponseDto>> getSecondHomePerfumeList() {
        return ResponseEntity.ok(homeMenuFacade.getHomeMenuSecond());
    }

    @ApiOperation("첫 번째 메뉴 전체보기")
    @GetMapping("/firstMenu")
    public ResponseEntity<List<HomeMenuAllResponseDto>> getFirstMenuAllPerfumeList(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token) {
        return ResponseEntity.ok(homeMenuFacade.getHomeMenuAllFirst(token));
    }

    @ApiOperation("두 번째 메뉴 전체보기")
    @GetMapping("/secondMenu")
    public ResponseEntity<List<HomeMenuAllResponseDto>> getSecondMenuAllPerfumeList(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token) {
        return ResponseEntity.ok(homeMenuFacade.getHomeMenuAllSecond(token));
    }

    @ApiOperation("세 번째 메뉴 전체보기")
    @GetMapping("/thirdMenu")
    public ResponseEntity<List<HomeMenuAllResponseDto>> getThirdMenuAllPerfumeList(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token) {
        return ResponseEntity.ok(homeMenuFacade.getHomeMenuAllThird(token));
    }
}
