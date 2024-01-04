package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentAllResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentModifyRequestDto;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.fcm.NotificationType;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
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

import static hmoa.hmoaserver.fcm.NotificationType.*;

@Api(tags = "커뮤니티 답변")
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comment")
public class CommunityCommentController {
    private final MemberService memberService;
    private final CommunityCommentService commentService;
    private final CommunityService communityService;
    private final FCMNotificationService fcmNotificationService;

    @ApiOperation("답변 저장")
    @PostMapping("/{communityId}/save")
    public ResponseEntity<CommunityCommentDefaultResponseDto> saveCommunityComment(@RequestHeader("X-AUTH-TOKEN")String token, @PathVariable Long communityId, @RequestBody CommunityCommentDefaultRequestDto dto){
        Member member = memberService.findByMember(token);
        Community community = communityService.getCommunityById(communityId);
        CommunityComment comment = commentService.saveCommunityComment(member, dto, community);
        fcmNotificationService.sendNotification(new FCMNotificationRequestDto(community.getMember().getId(), member.getNickname(), member.getId(), COMMUNITY_COMMENT));
        CommunityCommentDefaultResponseDto result = new CommunityCommentDefaultResponseDto(comment,true);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("답변 조회")
    @PostMapping("/{communityId}/findAll")
    public ResponseEntity<CommunityCommentAllResponseDto> findAllCommunityComment(@RequestHeader(value = "X-AUTH-TOKEN",required = false) String token, @PathVariable Long communityId,@RequestParam int page){
        Page<CommunityComment> comments = commentService.findAllCommunityComment(communityId,page);
        Member member = null;
        if(!memberService.isTokenNullOrEmpty(token)){
            member = memberService.findByMember(token);
        }
        Member finalMember = member;
        List<CommunityCommentDefaultResponseDto> commentDto = comments.stream().map(comment -> new CommunityCommentDefaultResponseDto(comment,comment.isWrited(finalMember))).collect(Collectors.toList());
        CommunityCommentAllResponseDto result = new CommunityCommentAllResponseDto(comments.getTotalElements(),commentDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("답변 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommunityCommentDefaultResponseDto> modifyCommunityComment(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long commentId, @RequestBody CommunityCommentModifyRequestDto dto){
        Member member = memberService.findByMember(token);
        CommunityComment comment = commentService.modifyCommunityComment(member,dto,commentId);
        return ResponseEntity.ok(new CommunityCommentDefaultResponseDto(comment,true));
    }

    @ApiOperation("답변 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResultDto> modifyCommunityComment(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long commentId){
        Member member = memberService.findByMember(token);
        String code = commentService.deleteCommunityComment(member,commentId);
        return ResponseEntity.ok(ResultDto.builder()
                .data(code)
                .build());
    }
}
