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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags="페이징 테스트")
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    @Value("${defalut.main}")
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

    @PostMapping("/perfume/testcreate")
    public void perfumeTestCreate(){testService.perfumeTest();}

    @GetMapping("/perfume/findtest")
    public ResponseEntity<MainResultDto<Object>> perfumeFindTest(){
        Page<Perfume> perfumes1 = testService.perfumeFindTest();
        Page<Perfume> perfumes2 = testService.perfumeFindTest2();
        List<MainTestDto> results1 = perfumes1.stream()
                .map(perfume -> new MainTestDto(perfume)).collect(Collectors.toList());
        List<MainTestDto> results2 = perfumes2.stream()
                .map(perfume -> new MainTestDto(perfume)).collect(Collectors.toList());
        TestDto test1= TestDto.builder().title("향모아 사용자들이 좋아한").perfumeList(results1).build();
        TestDto test2= TestDto.builder().title("향모아 사용자들이 사랑한").perfumeList(results2).build();
        TestDto test3= TestDto.builder().title("향모아 사용자들이 즐겨찾는").perfumeList(results2).build();
        TestDto test4= TestDto.builder().title("향모아 사용자들이 즐기는").perfumeList(results2).build();
        List<TestDto> results = new ArrayList<>();
        results.add(test1);
        results.add(test2);
        results.add(test3);
        results.add(test4);
        return ResponseEntity.status(200)
                .body(MainResultDto.builder()
                        .mainImage(DEFALUT_MAIN)
                        .recommend(results)
                        .build());
    }
}
