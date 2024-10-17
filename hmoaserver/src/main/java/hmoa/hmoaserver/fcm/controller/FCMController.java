package hmoa.hmoaserver.fcm.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import hmoa.hmoaserver.fcm.dto.FCMTestRequestDto;
import hmoa.hmoaserver.fcm.dto.FCMTokenSaveRequestDto;
import hmoa.hmoaserver.fcm.dto.PushAlarmResponseDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "푸쉬 알림 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<ResultDto<Object>> findPushAlarms(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        Page<PushAlarm> pushAlarms = fcmNotificationService.findPushAlarms(member);
        List<PushAlarmResponseDto> result = pushAlarms.stream().map(PushAlarmResponseDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(ResultDto.builder().data(result).build());
    }

    @ApiOperation(value = "푸시 알림 테스트 (단순 알림 받아 보기용)")
    @PostMapping("/test")
    public ResponseEntity<ResultDto<Object>> testPushAlarm(@RequestBody FCMTestRequestDto dto) {
        PushAlarm pushAlarm = fcmNotificationService.testNotification(dto);
        PushAlarmResponseDto result = new PushAlarmResponseDto(pushAlarm);

        return ResponseEntity.ok(ResultDto.builder().data(result).build());
    }

    @ApiOperation(value = "푸시 알림 테스트 (리스트에서 볼 수 있도록 저장까지 되는)")
    @PostMapping("/test-save")
    public ResponseEntity<ResultDto<Object>> testPushAlarmSave(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody FCMTestRequestDto dto) {
        PushAlarm pushAlarm = fcmNotificationService.testNotification(dto, token);
        PushAlarmResponseDto result = new PushAlarmResponseDto(pushAlarm);

        return ResponseEntity.ok(ResultDto.builder().data(result).build());
    }

    @ApiOperation(value = "푸쉬 알림 읽음 표시")
    @PutMapping("/read/{alarmId}")
    public ResponseEntity<ResultDto<Object>> readPushAlarm(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long alarmId) {
        Member member = memberService.findByMember(token);
        PushAlarm pushAlarm = fcmNotificationService.findById(alarmId);

        if (!member.isSameMember(pushAlarm.getMember())) {
            throw new CustomException(null, Code.FORBIDDEN_AUTHORIZATION);
        }

        fcmNotificationService.readPushAlarm(pushAlarm);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "fcm 토큰 저장")
    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> saveFcmToken(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody FCMTokenSaveRequestDto dto) {
        Member member = memberService.findByMember(token);
        log.info("{}", dto.getFCMToken());
        memberService.updateFCMToken(member, dto.getFCMToken());
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "fcm 토큰 제거")
    @DeleteMapping("/delete")
    public ResponseEntity<ResultDto<Object>> deleteFcmToken(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        memberService.deleteFCMToken(member);
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
