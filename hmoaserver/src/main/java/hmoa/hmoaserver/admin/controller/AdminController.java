package hmoa.hmoaserver.admin.controller;

import hmoa.hmoaserver.admin.dto.PerfumeByHomeSaveRequestDto;
import hmoa.hmoaserver.admin.service.PerfumeByHomeService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
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
    private final PerfumeByHomeService perfumeByHomeService;

    @PostMapping("/homePerfume")
    public ResponseEntity<ResultDto> saveHomePerfume(@RequestHeader("X-AUTH-TOKEN") String token , @RequestBody PerfumeByHomeSaveRequestDto dto){
        Member member = memberService.findByMember(token);
        perfumeByHomeService.save(member,dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
