package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.hshop.dto.BootpayCancelRequstDto;
import hmoa.hmoaserver.hshop.dto.BootpayConfirmRequestDto;
import hmoa.hmoaserver.hshop.service.BootpayService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Api(tags = {"Bootpay"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bootpay")
public class BootpayController {

    private final BootpayService bootpayService;
    private final MemberService memberService;

    @PostMapping("/confirm")
    public ResponseEntity<ResultDto<Object>> confirm(@RequestHeader("X-AUTH-TOKEN") String token, @Valid @RequestBody BootpayConfirmRequestDto dto) {
        HashMap res = bootpayService.checkPayment(dto);
        log.info("{}", res);

        return ResponseEntity.ok(ResultDto.builder()
                .data(res)
                .build());
    }

    @PostMapping("/cancel")
    public ResponseEntity<ResultDto<Object>> cancel(@RequestHeader("X-AUTH-TOKEN") String token, @Valid @RequestBody BootpayCancelRequstDto dto) {
        Member member = memberService.findByMember(token);
        HashMap res = bootpayService.cancelPayment(dto, member);

        return ResponseEntity.ok(ResultDto.builder()
                .data(res)
                .build());
    }
}
