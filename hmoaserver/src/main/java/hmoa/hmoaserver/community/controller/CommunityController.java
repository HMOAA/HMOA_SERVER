package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityByCategoryResponseDto;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityDefaultResponseDto;
import hmoa.hmoaserver.community.service.CommunityServiceImpl;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "커뮤니티")
@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final MemberService memberService;
    private final CommunityServiceImpl communityService;

    @ApiOperation("게시글 저장")
    @PostMapping("/save")
    public ResponseEntity<CommunityDefaultResponseDto> saveCommunity(@RequestHeader("X-AUTH-TOKEN") String token , @RequestBody CommunityDefaultRequestDto dto){
        Member member = memberService.findByMember(token);
        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(communityService.saveCommunity(member,dto),true);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("카테고리 별 게시글 조회")
    @GetMapping("/category")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findAllCommunity(@RequestParam Category category,@RequestParam int page){
        Page<Community> communities = communityService.getAllCommunitysByCategory(page,category);
        List<CommunityByCategoryResponseDto> result = communities.stream().map(community -> new CommunityByCategoryResponseDto(community)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @ApiOperation("게시글 단건 조회")
    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDefaultResponseDto> findOneCommunity(@RequestHeader(value = "X-AUTH-TOKEN",required = false) String token,@PathVariable Long communityId){
        boolean writed = false;
        Community community = communityService.getCommunityById(communityId);
        Member member = memberService.findByMember(token);
        if (token != null && token != "" && member.getId()==community.getMember().getId()){
            writed = true;
        }
        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(communityService.getCommunityById(communityId),writed);
        return ResponseEntity.ok(result);
    }
}
