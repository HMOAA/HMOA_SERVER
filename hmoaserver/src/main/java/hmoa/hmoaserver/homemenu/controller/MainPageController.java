package hmoa.hmoaserver.homemenu.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuDefaultResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuFirstResponseDto;
import hmoa.hmoaserver.homemenu.service.HomeMenuService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.homemenu.dto.HomeMenuPerfumeResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"메인 페이지"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Slf4j
public class MainPageController {
    private static final long FIRST_HOME_ID = 1L;
    private static final long SECOND_HOME_ID = 2L;
    private static final long THIRD_HOME_ID = 3L;

    private final HomeMenuService homeMenuService;

    @GetMapping("/first")
    public ResponseEntity<HomeMenuFirstResponseDto> getHomePerfumeList() {
        HomeMenu homeMenu = homeMenuService.findHomeMenuById(FIRST_HOME_ID);
        List<Perfume> perfumes = homeMenuService.findPerfumesByHomeMenu(homeMenu);
        Page<Perfume> perfumePage = new PageUtil<Perfume>().convertListToPage(perfumes, 0, 5);
        List<HomeMenuPerfumeResponseDto> perfumeResponseDtos = perfumePage.stream().map(HomeMenuPerfumeResponseDto::new).collect(Collectors.toList());
        HomeMenuFirstResponseDto result = new HomeMenuFirstResponseDto(new HomeMenuDefaultResponseDto(homeMenu.getTitle(), perfumeResponseDtos));
        return ResponseEntity.ok(result);
    }
}
