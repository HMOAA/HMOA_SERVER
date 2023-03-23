package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.dto.MemberReslutDto;
import hmoa.hmoaserver.member.dto.TokenResponseDto;
import hmoa.hmoaserver.member.dto.UpdateFirstDto;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.Token;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final JwtService jwtService;
    private final MemberService memberService;

    @GetMapping("/login/remembered")
    public ResponseEntity<TokenResponseDto> rememberedLogin(HttpServletRequest request){
        if (jwtService.extractRefreshToken(request).isPresent()){
            Token token = memberService.reIssue(jwtService.extractRefreshToken(request).get());
            TokenResponseDto responseDto = new TokenResponseDto(token);
            return ResponseEntity.ok(responseDto);
        }else {
            throw new RuntimeException("토큰을 보내주세요");
        }
    }

    @GetMapping("/jwt-test")
    public String jwtTest(){
        log.info("jwt-test");

        return "jwt-test";
    }

    /**
     * 멤버 단건 조회
     */
    @GetMapping("/member")
    public ResponseEntity<ResultDto<Object>> findOneMember(HttpServletRequest request,@RequestHeader("X-AUTH-TOKEN") String token) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        log.info("{}",email);

        MemberReslutDto reslutDto = new MemberReslutDto(findMember);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("MEMBER_FOUND")
                        .message("member 단건 조회")
                        .data(reslutDto)
                        .build());

    }

    @PatchMapping("/member/first")
    public ResponseEntity<ResultDto<Object>> firstMember(@RequestBody UpdateFirstDto request,@RequestHeader("X-AUTH-TOKEN") String token){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        memberService.firstMember(findMember,request.getAge(),request.getSex(),request.getNickname());
        MemberReslutDto reslutDto = new MemberReslutDto(findMember);
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("FIRST_MEMBER")
                        .message("첫 회원가입 시 정보 수정")
                        .data(reslutDto)
                        .build());
    }
}
