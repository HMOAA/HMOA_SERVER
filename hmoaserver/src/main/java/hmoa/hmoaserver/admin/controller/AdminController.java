package hmoa.hmoaserver.admin.controller;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuSaveRequestDto;
import hmoa.hmoaserver.homemenu.service.HomeMenuService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"관리자 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final MemberService memberService;
    private final HomeMenuService homeMenuService;

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
        Member member = memberService.findByMember(token);
        homeMenuService.addPerfumeForHomeMenu(perfumeId,homeId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("홈 메뉴 타이틀에서 향수 제거")
    @DeleteMapping("/homePerfume/delete")
    public ResponseEntity<ResultDto> deleteHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long perfumeId){
        homeMenuService.deleteHomeMenu(perfumeId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("홈 메뉴 타이틀 내용 수정")
    @PostMapping("/{homeMenuId}/modify")
    public ResponseEntity<ResultDto> modifyHomeMenu(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long homeMenuId, @RequestBody HomeMenuSaveRequestDto homeMenuSaveRequestDto) {
        HomeMenu homeMenu = homeMenuService.findHomeMenuById(homeMenuId);
        homeMenuService.modifyHomeMenu(homeMenu, homeMenuSaveRequestDto.getTitle());
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
