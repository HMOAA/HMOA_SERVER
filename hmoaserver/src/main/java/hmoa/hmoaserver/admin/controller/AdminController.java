package hmoa.hmoaserver.admin.controller;

import hmoa.hmoaserver.admin.dto.HomeMenuSaveRequestDto;
import hmoa.hmoaserver.admin.service.HomeMenuService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @ApiOperation("해당 타이틀에 추가할 향수")
    @PostMapping("/homePerfume/add")
    public ResponseEntity<ResultDto> addHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long perfumeId, @RequestParam Long homeId){
        Member member = memberService.findByMember(token);
        homeMenuService.addPerfumeForHomeMenu(perfumeId,homeId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("해당 타이틀에서 향수 제거")
    @DeleteMapping("/homePerfume/delete")
    public ResponseEntity<ResultDto> deleteHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long perfumeId){
        homeMenuService.deleteHomeMenu(perfumeId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
