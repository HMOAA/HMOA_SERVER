package hmoa.hmoaserver.member.controller;

import hmoa.hmoaserver.common.MainResultDto;
import hmoa.hmoaserver.common.MainTestDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.common.TestDto;
import hmoa.hmoaserver.member.domain.Member;

import hmoa.hmoaserver.member.dto.MemberResponseDto;
import hmoa.hmoaserver.member.service.TestService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags="페이징 테스트")
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    @Value("${default.main}")
    private String DEFALUT_MAIN;
    private final TestService testService;

    @PostMapping("/member/testcreate")
    public void testCreate(){
        testService.createTest();
    }

    @GetMapping("/member/testcreate")
    public ResponseEntity<ResultDto<Object>> findTest(){
        Page<Member> members = testService.findTest();
        List<MemberResponseDto> results = members.stream()
                .map(member -> new MemberResponseDto(member)).collect(Collectors.toList());
        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(results)
                        .build());
    }

    @ApiOperation(value = "테스트 향수 목록 만들기")
    @PostMapping("/perfume/testcreate")
    public void perfumeTestCreate(){testService.perfumeTest();}

    @ApiOperation(value = "메인 페이지 호출 1")
    @GetMapping("/perfume/findtest")
    public ResponseEntity<MainResultDto<Object>> perfumeFindTest(){
        Page<Perfume> perfumes = testService.perfumeFindTest();
        List<MainTestDto> results1 = perfumes.stream()
                .map(perfume -> new MainTestDto(perfume)).collect(Collectors.toList());
        TestDto test1= TestDto.builder().title("향모아 사용자들이 좋아한").perfumeList(results1).build();
        return ResponseEntity.status(200)
                .body(MainResultDto.builder()
                        .mainImage(DEFALUT_MAIN)
                        .recommend(test1)
                        .build());
    }
    @ApiOperation(value = "메인 페이지 호출 2")
    @GetMapping("/perfume/findtest2")
    public ResponseEntity<ResultDto<Object>> perfumeFindTest2(){
        Page<Perfume> perfumes = testService.perfumeFindTest2();
        List<MainTestDto> results = perfumes.stream()
                .map(perfume -> new MainTestDto(perfume)).collect(Collectors.toList());
        TestDto test1 = TestDto.builder().title("향모아 사용자들이 사랑한").perfumeList(results).build();
        TestDto test2 = TestDto.builder().title("향모아 사용자들이 즐겨찾는").perfumeList(results).build();
        TestDto test3 = TestDto.builder().title("향모아 사용자들이 즐기는").perfumeList(results).build();
        List<TestDto> dtos = new ArrayList<>();
        dtos.add(test1);
        dtos.add(test2);
        dtos.add(test3);
        return ResponseEntity.ok(ResultDto.builder()
                .data(dtos)
                .build());
    }

    @GetMapping("/perfume/testTime")
    public ResponseEntity<ResultDto<Object>> timeTest() {
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(ResultDto.builder()
                .data(now)
                .build());
    }
}
