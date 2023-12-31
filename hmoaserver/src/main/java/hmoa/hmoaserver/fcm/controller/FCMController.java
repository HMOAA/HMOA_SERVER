package hmoa.hmoaserver.fcm.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.dto.FCMTokenSaveRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/fcm")
public class FCMController {
    private final FCMNotificationService fcmNotificationService;
    private final MemberService memberService;


//    @PostMapping("/test")
//    public String test(@RequestBody FCMNotificationRequestDto dto) {
//        return fcmNotificationService.sendNotification(dto);
//    }

    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> saveFcmToken(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody FCMTokenSaveRequestDto dto) {
        Member member = memberService.findByMember(token);
        log.info("{}", dto.getFCMToken());
        memberService.updateFCMToken(member, dto.getFCMToken());
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResultDto<Object>> deleteFcmToken(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        memberService.deleteFCMToken(member);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
