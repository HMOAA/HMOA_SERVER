package hmoa.hmoaserver.oauth.apple;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.fcm.dto.FCMTokenSaveRequestDto;
import hmoa.hmoaserver.oauth.service.AppleLoginService;
import hmoa.hmoaserver.oauth.userinfo.AppleOAuth2UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"애플 로그인 테스트"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/appleTest")
@Slf4j
public class AppleTestController {
    private final AppleLoginService appleLoginService;

    @PostMapping("/test")
    public ResponseEntity<ResultDto<Object>> testApple(@RequestBody FCMTokenSaveRequestDto dto) {
        String token = dto.getFCMToken();
        AppleOAuth2UserInfo result = appleLoginService.get(token);

        return ResponseEntity.ok(ResultDto.builder().data(result).build());
    }
}
