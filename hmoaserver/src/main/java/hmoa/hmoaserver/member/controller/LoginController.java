package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.exception.ExceptionResponseDto;
import hmoa.hmoaserver.member.domain.ProviderType;
import hmoa.hmoaserver.member.dto.MemberLoginResponseDto;
import hmoa.hmoaserver.member.dto.TokenResponseDto;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.AccessToken;
import hmoa.hmoaserver.oauth.jwt.Token;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static hmoa.hmoaserver.exception.Code.UNKNOWN_ERROR;

@Api(tags="로그인")
@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;
    private final MemberService memberService;

    @ApiOperation(value = "자동 로그인")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "성공 응답",
                    response = TokenResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "토큰이 없거나 잘못됐습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "접근 권한이 없습니다",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "일치하는 회원이 없습니다.",
                    response = ExceptionResponseDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "서버 에러입니다.",
                    response = ExceptionResponseDto.class
            )
    })
    @GetMapping("/login/remembered")
    public ResponseEntity<TokenResponseDto> rememberedLogin(@RequestHeader("rememberedToken") String rememberedToken) {
        if (!rememberedToken.isEmpty()) {
            Token token = memberService.reIssue(rememberedToken);
            TokenResponseDto responseDto = new TokenResponseDto(token);
            return ResponseEntity.ok(responseDto);
        } else {
            throw new CustomException(null, UNKNOWN_ERROR);
        }
    }

    @PostMapping("/login/oauth2/{provider}")
    public ResponseEntity<MemberLoginResponseDto> loginSocial(@RequestBody AccessToken accessToken, @PathVariable ProviderType provider) {
        log.info("{}",accessToken.getToken());
        log.info("{}",provider);
        MemberLoginResponseDto responseDto = memberService.loginMember(accessToken.getToken(), provider);
        return ResponseEntity.ok(responseDto);
    }

}
