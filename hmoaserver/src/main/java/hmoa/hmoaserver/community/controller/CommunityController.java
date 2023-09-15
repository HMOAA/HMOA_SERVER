package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityDefaultResponseDto;
import hmoa.hmoaserver.community.service.CommunityServiceImpl;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "커뮤니티")
@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final MemberService memberService;
    private final CommunityServiceImpl communityService;

    @PostMapping("/save")
    public ResponseEntity<CommunityDefaultResponseDto> saveCommunity(@RequestHeader("X-AUTH-TOKEN") String token , @RequestBody CommunityDefaultRequestDto dto){
        Member member = memberService.findByMember(token);
        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(communityService.saveCommunity(member,dto));
        return ResponseEntity.ok(result);
    }
}
