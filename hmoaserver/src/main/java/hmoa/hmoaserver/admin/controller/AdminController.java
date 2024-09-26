package hmoa.hmoaserver.admin.controller;

import hmoa.hmoaserver.admin.dto.AdminTokenRequestDto;
import hmoa.hmoaserver.admin.dto.OrderDeliverySaveRequestDto;
import hmoa.hmoaserver.admin.AdminFacade;
import hmoa.hmoaserver.admin.dto.OrderStatusUpdateRequestDto;
import hmoa.hmoaserver.admin.dto.TrackingCallbackRequestDto;
import hmoa.hmoaserver.admin.service.TestTokenProvider;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuSaveRequestDto;
import hmoa.hmoaserver.homemenu.service.HomeMenuService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.homemenu.service.PerfumeHomeMenuService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(tags = {"관리자 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final TestTokenProvider testTokenProvider;
    @Value("${jwt.admin}")
    private String admin;

    private final MemberService memberService;
    private final HomeMenuService homeMenuService;
    private final PerfumeHomeMenuService perfumeHomeMenuService;
    private final PerfumeService perfumeService;
    private final AdminFacade adminFacade;

    @ApiOperation("홈 메뉴 타이틀 추가")
    @PostMapping("/homePerfume")
    public ResponseEntity<ResultDto> saveHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token , @RequestBody HomeMenuSaveRequestDto dto){
        Member member = memberService.findByMember(token);
        homeMenuService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("홈 메뉴 타이틀에 추가할 향수")
    @PostMapping("/homePerfume/add")
    public ResponseEntity<ResultDto> addHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long perfumeId, @RequestParam Long homeId){
        HomeMenu homeMenu = homeMenuService.getHomeMenu(homeId);
        Perfume perfume = perfumeService.findById(perfumeId);
        perfumeHomeMenuService.save(homeMenu, perfume);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("홈 메뉴 초기화")
    @DeleteMapping("/{homeMenuId}/delete")
    public ResponseEntity<ResultDto> deleteHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long homeMenuId){
        HomeMenu homeMenu = homeMenuService.getHomeMenu(homeMenuId);
        perfumeHomeMenuService.reset(homeMenu);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("홈 메뉴 타이틀 내용 수정")
    @PostMapping("/{homeMenuId}/modify")
    public ResponseEntity<ResultDto> modifyHomeMenu(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long homeMenuId, @RequestBody HomeMenuSaveRequestDto homeMenuSaveRequestDto) {
        HomeMenu homeMenu = homeMenuService.getHomeMenu(homeMenuId);
        homeMenuService.modifyHomeMenu(homeMenu, homeMenuSaveRequestDto.getTitle());
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("관리자 토큰 발급")
    @PostMapping("/admin-token")
    public ResponseEntity<?> getAdminToken(@RequestBody AdminTokenRequestDto dto) {
        if (dto.getSecret().equals(admin)) {
            return ResponseEntity.ok(testTokenProvider.getTestToken());
        }

        throw new CustomException(null, Code.FORBIDDEN_AUTHORIZATION);
    }

    // 운송장 등록 + Tracking delivery 서비스 등록
    @ApiOperation("운송장 등록")
    @PostMapping("/delivery-info")
    public ResponseEntity<?> saveMemberAddress(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody OrderDeliverySaveRequestDto dto) {
        adminFacade.saveDeliveryInfo(dto);
        Mono<String> data = adminFacade.registerTrackWebhook(dto);
        return ResponseEntity.ok(ResultDto.builder().data(data).build());
    }

    @ApiOperation("배송 변화 감지")
    @PostMapping("/delivery/check")
    public ResponseEntity<?> checkTracking(@RequestBody TrackingCallbackRequestDto dto) {
        adminFacade.checkTracking(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("OrderStatus 수정")
    @PutMapping("/order")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderStatusUpdateRequestDto dto) {
        adminFacade.updateOrderStatus(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
