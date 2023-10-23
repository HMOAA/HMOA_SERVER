package hmoa.hmoaserver.perfume.controller;

import hmoa.hmoaserver.admin.service.HomeMenuService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.HomeMenuPerfumeResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final HomeMenuService homeMenuService;

    @GetMapping("/first")
    public ResponseEntity<List<HomeMenuPerfumeResponseDto>> getHomePerfumeList(@RequestParam Long homeId){
        List<Perfume> perfumes = homeMenuService.findPerfumesByHomeMenu(homeId);
        List<HomeMenuPerfumeResponseDto> result = perfumes.stream().map(perfume -> new HomeMenuPerfumeResponseDto(perfume)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
