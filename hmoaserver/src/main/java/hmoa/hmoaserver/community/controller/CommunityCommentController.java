package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentAllResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultResponseDto;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.community.service.CommunityService;
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

@Api(tags = "커뮤니티 답변")
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comment")
public class CommunityCommentController {
    private final MemberService memberService;
    private final CommunityCommentService commentService;
    private final CommunityService communityService;

    @ApiOperation("답변 저장")
    @PostMapping("/{communityId}/save")
    public ResponseEntity<CommunityCommentDefaultResponseDto> saveCommunityComment(@RequestHeader("X-AUTH-TOKEN")String token, @PathVariable Long communityId, @RequestBody CommunityCommentDefaultRequestDto dto){
        Member member = memberService.findByMember(token);
        Community community = communityService.getCommunityById(communityId);
        CommunityCommentDefaultResponseDto result = new CommunityCommentDefaultResponseDto(commentService.saveCommunityComment(member,dto,community),true);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("답변 조회")
    @PostMapping("/{communityId}/findAll")
    public ResponseEntity<CommunityCommentAllResponseDto> findAllCommunityComment(@RequestHeader(value = "X-AUTH-TOKEN",required = false) String token, @PathVariable Long communityId,@RequestParam int page){
        Page<CommunityComment> comments = commentService.getCommunityComment(communityId,page);
        Member member = null;
        if(!memberService.isTokenNullOrEmpty(token)){
            member = memberService.findByMember(token);
        }
        Member finalMember = member;
        List<CommunityCommentDefaultResponseDto> commentDto = comments.stream().map(comment -> new CommunityCommentDefaultResponseDto(comment,comment.isWrited(finalMember))).collect(Collectors.toList());
        CommunityCommentAllResponseDto result = new CommunityCommentAllResponseDto(comments.getTotalElements(),commentDto);
        return ResponseEntity.ok(result);
    }
}
