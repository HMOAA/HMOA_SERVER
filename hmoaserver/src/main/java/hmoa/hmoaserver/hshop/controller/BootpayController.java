package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.dto.BootpayCancelRequstDto;
import hmoa.hmoaserver.hshop.dto.BootpayConfirmRequestDto;
import hmoa.hmoaserver.hshop.service.BootpayService;
import hmoa.hmoaserver.hshop.service.OrderService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private final OrderService orderService;
    private final MemberService memberService;

    @ApiOperation(value = "구매 승인하기")
    @PostMapping("/confirm")
    public ResponseEntity<ResultDto<Object>> confirm(@RequestHeader("X-AUTH-TOKEN") String token, @Valid @RequestBody BootpayConfirmRequestDto dto) {
        HashMap res = bootpayService.checkPayment(dto);

        return ResponseEntity.ok(ResultDto.builder()
                .data(res)
                .build());
    }

    @ApiOperation(value = "주문 취소 (receiptId로, 사용 X)")
    @PostMapping("/cancel")
    public ResponseEntity<ResultDto<Object>> cancel(@RequestHeader("X-AUTH-TOKEN") String token, @Valid @RequestBody BootpayCancelRequstDto dto) {
        Member member = memberService.findByMember(token);
        HashMap res = bootpayService.cancelPayment(dto.getReceiptId(), dto.getCancelReason(), member);

        return ResponseEntity.ok(ResultDto.builder()
                .data(res)
                .build());
    }

    @ApiOperation(value = "주문 취소 (orderId로)")
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<ResultDto<Object>> cancel(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable("orderId") Long orderId) {
        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);

        if (!member.getId().equals(order.getMemberId())) {
            throw new CustomException(null, Code.FORBIDDEN_AUTHORIZATION);
        }
        HashMap res = bootpayService.cancelPayment(order.getReceiptId(), "단순 변심", member);

        return ResponseEntity.ok(ResultDto.builder().data(res).build());
    }
}
