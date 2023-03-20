package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final JwtService jwtService;

    @GetMapping("/login/remembered")
    public Map<String,String> rememberedLogin(){
        Map<String,String> map =new HashMap<>();
        return map;
    }

    @GetMapping("/jwt-test")
    public String jwtTest(){
        return "jwt-test";
    }
}
