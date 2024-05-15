package hmoa.hmoaserver.fcm.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.dto.FCMTokenSaveRequestDto;
import hmoa.hmoaserver.fcm.dto.PushAlarmResponseDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/fcm")
public class FCMController {
    private final FCMNotificationService fcmNotificationService;
    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<PushAlarmResponseDto>> findPushAlarms(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        Page<PushAlarm> pushAlarms = fcmNotificationService.findPushAlarms(member);
        List<PushAlarmResponseDto> result = pushAlarms.stream().map(PushAlarmResponseDto::new).collect(Collectors.toList());

        pushAlarms.forEach(fcmNotificationService::readPushAlarm);
        
        return ResponseEntity.ok(pushAlarms.stream().map(PushAlarmResponseDto::new).collect(Collectors.toList()));
    }

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
