package hmoa.hmoaserver.admin.controller;

import hmoa.hmoaserver.admin.dto.PerfumeByHomeSaveRequestDto;
import hmoa.hmoaserver.admin.service.HomeMenuService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;

@Api(tags = {"관리자 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final MemberService memberService;
    private final HomeMenuService homeMenuService;

    @PostMapping("/homePerfume")
    public ResponseEntity<ResultDto> saveHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token , @RequestBody PerfumeByHomeSaveRequestDto dto){
        Member member = memberService.findByMember(token);
        homeMenuService.save(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @PostMapping("/homePerfume/add")
    public ResponseEntity<ResultDto> addHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long perfumeId, @RequestParam Long homeId){
        Member member = memberService.findByMember(token);
        homeMenuService.addPerfumeForHomeMenu(perfumeId,homeId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @DeleteMapping("/homePerfume/delete")
    public ResponseEntity<ResultDto> deleteHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long perfumeId){
        homeMenuService.deleteHomeMenu(perfumeId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

}
