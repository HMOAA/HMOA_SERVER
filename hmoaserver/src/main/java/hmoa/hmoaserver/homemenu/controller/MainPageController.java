package hmoa.hmoaserver.homemenu.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuDefaultResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuFirstResponseDto;
import hmoa.hmoaserver.homemenu.service.HomeMenuService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.homemenu.dto.HomeMenuPerfumeResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private static final long BRAND_HOME_ID = 1L;
    private static final long FIRST_HOME_ID = 2L;
    private static final long SECOND_HOME_ID = 3L;
    private static final long THIRD_HOME_ID = 4L;

    private final HomeMenuService homeMenuService;

    @ApiOperation("메인 페이지 첫 번째")
    @GetMapping("/first")
    public ResponseEntity<HomeMenuFirstResponseDto> getHomePerfumeList() {
        HomeMenu homeMenu = homeMenuService.findHomeMenuById(BRAND_HOME_ID);
        List<Perfume> perfumes = homeMenuService.findPerfumesByHomeMenu(homeMenu);
        Page<Perfume> perfumePage = new PageUtil<Perfume>().convertListToPage(perfumes, 0, 5);
        List<HomeMenuPerfumeResponseDto> perfumeResponseDtos = perfumePage.stream().map(HomeMenuPerfumeResponseDto::new).collect(Collectors.toList());
        HomeMenuFirstResponseDto result = new HomeMenuFirstResponseDto(new HomeMenuDefaultResponseDto(homeMenu.getTitle(), perfumeResponseDtos));
        return ResponseEntity.ok(result);
    }

    @ApiOperation("메인 페이지 두 번째")
    @GetMapping("/second")
    public ResponseEntity<List<HomeMenuDefaultResponseDto>> getSecondHomePerfumeList() {
        HomeMenu firstMenu = homeMenuService.findHomeMenuById(FIRST_HOME_ID);
        HomeMenu secondMenu = homeMenuService.findHomeMenuById(SECOND_HOME_ID);
        HomeMenu thirdMenu = homeMenuService.findHomeMenuById(THIRD_HOME_ID);

        List<HomeMenuPerfumeResponseDto> firstPerfumesInfo = getPerfumeResponseDto(firstMenu.getPerfumeList());
        List<HomeMenuPerfumeResponseDto> secondPerfumesInfo = getPerfumeResponseDto(secondMenu.getPerfumeList());
        List<HomeMenuPerfumeResponseDto> thirdPefumesInfo = getPerfumeResponseDto(thirdMenu.getPerfumeList());

        HomeMenuDefaultResponseDto firstDto = new HomeMenuDefaultResponseDto(firstMenu.getTitle(), firstPerfumesInfo);
        HomeMenuDefaultResponseDto secondDto = new HomeMenuDefaultResponseDto(secondMenu.getTitle(), secondPerfumesInfo);
        HomeMenuDefaultResponseDto thirdDto = new HomeMenuDefaultResponseDto(thirdMenu.getTitle(), thirdPefumesInfo);

        return ResponseEntity.ok(List.of(firstDto, secondDto, thirdDto));
    }

    private static List<HomeMenuPerfumeResponseDto> getPerfumeResponseDto(List<Perfume> perfumes) {
        return convertListToPage(perfumes).stream().map(HomeMenuPerfumeResponseDto::new).collect(Collectors.toList());
    }

    private static Page<Perfume> convertListToPage(List<Perfume> perfumes) {
        return new PageUtil<Perfume>().convertListToPage(perfumes, 0, 5);
    }
}
