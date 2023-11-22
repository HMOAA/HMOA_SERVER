package hmoa.hmoaserver.fcm.controller;

import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fcm")
public class FCMController {
    private final FCMNotificationService fcmNotificationService;

    @PostMapping("/test")
    public String test(@RequestBody FCMNotificationRequestDto dto) {
        return fcmNotificationService.sendNotificationByToken(dto);
    }

}
