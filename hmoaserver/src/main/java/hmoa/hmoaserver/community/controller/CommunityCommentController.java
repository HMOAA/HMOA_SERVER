package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.community.dto.*;
import hmoa.hmoaserver.community.service.CommunityCommentLikedMemberService;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.community.service.CommunityService;
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

import static hmoa.hmoaserver.fcm.service.constant.NotificationType.*;

@Api(tags = "커뮤니티 답변")
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comment")
public class CommunityCommentController {
    private final MemberService memberService;
    private final CommunityCommentService commentService;
    private final CommunityService communityService;
    private final CommunityCommentLikedMemberService commentLikedMemberService;
    private final FCMNotificationService fcmNotificationService;

    @ApiOperation("답변 저장")
    @PostMapping("/{communityId}/save")
    public ResponseEntity<CommunityCommentDefaultResponseDto> saveCommunityComment(@RequestHeader("X-AUTH-TOKEN")String token, @PathVariable Long communityId, @RequestBody CommunityCommentDefaultRequestDto dto) {
        Member member = memberService.findByMember(token);
        Community community = communityService.getCommunityById(communityId);
        CommunityComment comment = commentService.saveCommunityComment(member, dto, community);
        fcmNotificationService.sendNotification(new FCMNotificationRequestDto(community.getMember().getId(), member.getNickname(), member.getId(), COMMUNITY_COMMENT, communityId));
        CommunityCommentDefaultResponseDto result = new CommunityCommentDefaultResponseDto(comment, true, false);

        return ResponseEntity.ok(result);
    }

    @ApiOperation("답변 단 건 조회")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommunityCommentDefaultResponseDto> findOneCommunityComment(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @PathVariable Long commentId) {
        CommunityComment comment = commentService.findOneComunityComment(commentId);
        if (memberService.isTokenNullOrEmpty(token)) {
            return ResponseEntity.ok(new CommunityCommentDefaultResponseDto(comment));
        }
        Member member = memberService.findByMember(token);
        return ResponseEntity.ok(new CommunityCommentDefaultResponseDto(comment, comment.isWrited(member), commentLikedMemberService.isCommentLikedMember(member, comment)));
    }

    @ApiOperation("답변 조회")
    @PostMapping("/{communityId}/findAll")
    public ResponseEntity<CommunityCommentAllResponseDto> findAllCommunityComment(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @PathVariable Long communityId, @RequestParam int page) {
        Page<CommunityComment> comments = commentService.findAllCommunityComment(communityId,page);
        boolean isLastPage = isLastPage(comments);

        if (memberService.isTokenNullOrEmpty(token)) {
            List<CommunityCommentDefaultResponseDto> commentDtos = comments.stream().map(CommunityCommentDefaultResponseDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(new CommunityCommentAllResponseDto(comments.getTotalElements(), isLastPage, commentDtos));
        }

        Member member = memberService.findByMember(token);
        List<CommunityCommentDefaultResponseDto> commentDtos = comments.stream().map(
                comment -> new CommunityCommentDefaultResponseDto(
                        comment, comment.isWrited(member), commentLikedMemberService.isCommentLikedMember(member, comment)
                )).collect(Collectors.toList());

        return ResponseEntity.ok(new CommunityCommentAllResponseDto(comments.getTotalElements(), isLastPage, commentDtos));
    }

    @ApiOperation(value = "답변 조회 (커서 페이징)", notes = "처음 Cursor는 0으로 보내기, 다음 Cursor는 마지막 Comment의 id 값 보내기.")
    @GetMapping("/{communityId}/findAll/cursor")
    public ResponseEntity<CommunityCommentAllResponseDto> findAllCommunityComment(@RequestHeader(value = "X-AUTH-TOKEN", required = false) String token, @PathVariable Long communityId, @RequestParam Long cursor) {
        Page<CommunityComment> comments = commentService.findAllCommunityComment(communityId, cursor);
        Long count = commentService.countAllCommunityComment(communityId);
        boolean isLastPage = PageUtil.isLastPage(comments);

        if (memberService.isTokenNullOrEmpty(token)) {
            List<CommunityCommentDefaultResponseDto> commentDtos = comments.stream().map(CommunityCommentDefaultResponseDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(new CommunityCommentAllResponseDto(count, isLastPage, commentDtos));
        }

        Member member = memberService.findByMember(token);
        List<CommunityCommentDefaultResponseDto> commentDtos = comments.stream().map(
                comment -> new CommunityCommentDefaultResponseDto(
                        comment, comment.isWrited(member), commentLikedMemberService.isCommentLikedMember(member, comment)
                )).collect(Collectors.toList());

        return ResponseEntity.ok(new CommunityCommentAllResponseDto(count, isLastPage, commentDtos));
    }

    @ApiOperation(value = "내가 쓴 커뮤니티 답변 (커서 페이징)")
    @GetMapping("/me")
    public ResponseEntity<PagingDto<Object>> findAllByMe(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if (PageUtil.isFistCursor(cursor)) cursor = PageUtil.convertFirstCursor(cursor);
        Page<CommunityComment> comments = commentService.findAllByMemberNextCursor(member, cursor);
        boolean isLastPage = PageUtil.isLastPage(comments);

        List<CommunityCommentByMemberResponseDto> result = comments.stream().map(comment ->
                new CommunityCommentByMemberResponseDto(comment, commentLikedMemberService.isCommentLikedMember(member, comment), true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(result).
                build());
    }

    @ApiOperation(value = "내가 좋아요한 커뮤니티 답변 (커서 페이징)")
    @GetMapping("/like")
    public ResponseEntity<PagingDto<Object>> findAllByLike(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if (PageUtil.isFistCursor(cursor)) cursor = PageUtil.convertFirstCursor(cursor);
        Page<CommunityCommentLikedMember> cclms = commentLikedMemberService.findAllByMemberAndCursor(member, cursor);
        boolean isLastPage = PageUtil.isLastPage(cclms);

        List<CommunityCommentByMemberResponseDto> result = cclms.stream().map(cclm ->
                new CommunityCommentByMemberResponseDto(cclm.getCommunityComment(), true, member.isSameMember(cclm.getCommunityComment().getMember())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(result)
                .build());
    }

    @ApiOperation("답변 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommunityCommentDefaultResponseDto> modifyCommunityComment(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long commentId, @RequestBody CommunityCommentModifyRequestDto dto){
        Member member = memberService.findByMember(token);
        CommunityComment comment = commentService.modifyCommunityComment(member,dto,commentId);

        return ResponseEntity.ok(new CommunityCommentDefaultResponseDto(
                        comment, true, commentLikedMemberService.isCommentLikedMember(member, comment)
                ));
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

    @ApiOperation("답변 좋아요 하기")
    @PutMapping("/{commentId}/like")
    public ResponseEntity<ResultDto> saveCommentLike(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long commentId) {
        Member member = memberService.findByMember(token);
        CommunityComment comment = commentService.findOneComunityComment(commentId);

        commentLikedMemberService.save(member, comment);
        fcmNotificationService.sendNotification(new FCMNotificationRequestDto(comment.getMember().getId(), member.getNickname(), member.getId(), COMMUNITY_COMMENT_LIKE, comment.getCommunity().getId()));

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("답변 좋아요 취소하기")
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ResultDto> deleteCommentLike(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long commentId) {
        Member member = memberService.findByMember(token);
        CommunityComment comment = commentService.findOneComunityComment(commentId);

        commentLikedMemberService.delete(member, comment);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    private static boolean isLastPage(Page<CommunityComment> comments) {
        return !comments.hasNext();
    }
}
