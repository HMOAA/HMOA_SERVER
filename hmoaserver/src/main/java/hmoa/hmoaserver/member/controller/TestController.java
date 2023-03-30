package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;

import hmoa.hmoaserver.member.dto.MemberReslutDto;
import hmoa.hmoaserver.member.service.TestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags="페이징 테스트")
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;


    @PostMapping("/member/testcreate")
    public void testCreate(){
        testService.createTest();
    }

    @GetMapping("/member/testcreate")
    public ResponseEntity<ResultDto<Object>> findTest(){
        Page<Member> members = testService.findTest();
        List<MemberReslutDto> results = members.stream()
                .map(member -> new MemberReslutDto(member)).collect(Collectors.toList());
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .resultCode("test")
                        .message("test완료")
                        .data(results)
                        .build());
    }
}
