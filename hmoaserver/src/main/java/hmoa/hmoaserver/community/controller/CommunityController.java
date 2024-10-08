package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityLikedMember;
import hmoa.hmoaserver.community.dto.*;
import hmoa.hmoaserver.community.service.CommunityLikedMemberService;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import hmoa.hmoaserver.photo.service.CommunityPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.fcm.service.constant.NotificationType.COMMUNITY_LIKE;

@Api(tags = "커뮤니티")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/community")
public class CommunityController {
    private final MemberService memberService;
    private final PhotoService photoService;
    private final CommunityPhotoService communityPhotoService;
    private final CommunityService communityService;
    private final CommunityLikedMemberService communityLikedMemberService;
    private final FCMNotificationService fcmNotificationService;

    @ApiOperation("게시글 저장")
    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<CommunityDefaultResponseDto> saveCommunity(HttpServletRequest request, @RequestPart(value="image", required = false) List<MultipartFile> files, @RequestHeader("X-AUTH-TOKEN") String token, CommunityDefaultRequestDto dto) {
        Member member = memberService.findByMember(token);
        Community community = communityService.saveCommunity(member, dto);
        if (files == null) {
            files = Collections.emptyList();
        }
        photoService.validateCommunityPhotoCountExceeded(files.size());

        if (files.size() != 0)
            communityService.saveCommunityPhotos(community, files);

        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(community);
        result.setWrited(true);
        result.setMyProfileImgUrl(member.getMemberPhoto().getPhotoUrl());

        return ResponseEntity.ok(result);
    }

    @ApiOperation("카테고리 별 게시글 조회")
    @GetMapping("/category")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findAllCommunity(@RequestHeader(name = "X-AUTH-TOKEN", required = false) String token, @RequestParam Category category, @RequestParam int page) {
        Page<Community> communities = communityService.getAllCommunitysByCategory(page,category);

        if (memberService.isTokenNullOrEmpty(token)) {
            return ResponseEntity.ok(communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList()));
        }

        Member member = memberService.findByMember(token);
        List<CommunityByCategoryResponseDto> result = communities
                .stream()
                .map(community -> new CommunityByCategoryResponseDto(community, communityLikedMemberService.isCommunityLikedMember(member, community)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "카테고리 별 게시글 조회 (커서 페이징)", notes = "처음 Cursor는 0으로 보내기, 다음 Cursor는 마지막 Comment의 id 값 보내기.")
    @GetMapping("/category/cursor")
    public ResponseEntity<CommunityListResponseDto> findAllCommunityByCursor(@RequestHeader(name = "X-AUTH-TOKEN", required = false) String token, @RequestParam Category category, @RequestParam Long cursor) {
        Page<Community> communities = communityService.getAllCommunitysByCategory(cursor, category);
        boolean isLastPage = PageUtil.isLastPage(communities);

        if (memberService.isTokenNullOrEmpty(token)) {
            return ResponseEntity.ok(new CommunityListResponseDto(isLastPage, communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList())));
        }

        Member member = memberService.findByMember(token);
        List<CommunityByCategoryResponseDto> result = communities
                .stream()
                .map(community -> new CommunityByCategoryResponseDto(community, communityLikedMemberService.isCommunityLikedMember(member, community)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CommunityListResponseDto(isLastPage, result));
    }

    @ApiOperation(value = "내가 쓴 게시글 조회 (커서 페이징)")
    @GetMapping("/me")
    public ResponseEntity<PagingDto<Object>> findAllCommunitiesByMember(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if(PageUtil.isFistCursor(cursor)) cursor = PageUtil.convertFirstCursor(cursor);
        Page<Community> communities = communityService.getCommunityByMemberAndCursor(member, cursor);
        boolean isLastPage = PageUtil.isLastPage(communities);

        List<CommunityByCategoryResponseDto> result = communities.stream().map(community ->
                new CommunityByCategoryResponseDto(community, communityLikedMemberService.isCommunityLikedMember(member, community)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(result)
                .build());
    }

    @ApiOperation(value = "내가 좋아요 한 게시글 조회 (커서 페이징)")
    @GetMapping("/like")
    public ResponseEntity<PagingDto<Object>> findAllCommunitiesByLiked(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if(PageUtil.isFistCursor(cursor)) cursor = PageUtil.convertFirstCursor(cursor);
        Page<CommunityLikedMember> clms = communityLikedMemberService.findAllByMember(member, cursor);
        boolean isLastPage = PageUtil.isLastPage(clms);

        List<CommunityByCategoryResponseDto> result = clms.stream().map(clm ->
                new CommunityByCategoryResponseDto(clm.getCommunity(), true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(result)
                .build());
    }

    @ApiOperation("커뮤니티 홈 조회")
    @GetMapping("/home")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findCommunitByHome(@RequestHeader(name = "X-AUTH-TOKEN", required = false) String token) {
        Page<Community> communities = communityService.getCommunityByHome();
        if (memberService.isTokenNullOrEmpty(token)) {
            return ResponseEntity.ok(communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList()));
        }

        Member member = memberService.findByMember(token);
        List<CommunityByCategoryResponseDto> result = communities
                .stream()
                .map(community -> new CommunityByCategoryResponseDto(community, communityLikedMemberService.isCommunityLikedMember(member, community)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @ApiOperation("커뮤니티 게시글 단건 조회")
    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDefaultResponseDto> findOneCommunity(@RequestHeader(value = "X-AUTH-TOKEN",required = false) String token, @PathVariable Long communityId) {
        Community community = communityService.getCommunityById(communityId);

        if(memberService.isTokenNullOrEmpty(token)){
            return ResponseEntity.ok(new CommunityDefaultResponseDto(community));
        }

        Member member = memberService.findByMember(token);
        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(community, community.isWrited(member), communityLikedMemberService.isCommunityLikedMember(member, community));
        result.setMyProfileImgUrl(member.getMemberPhoto().getPhotoUrl());

        return ResponseEntity.ok(result);
    }

    @ApiOperation("커뮤니티 내용 수정")
    @PostMapping(value = "/{communityId}", consumes = "multipart/form-data")
    public ResponseEntity<CommunityDefaultResponseDto> modifyCommunity(@RequestPart(value="image", required = false) List<MultipartFile> files, @RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long communityId, CommunityModifyRequestDto dto){
        Member member = memberService.findByMember(token);
        Community community = communityService.getCommunityById(communityId);

        if (files == null) {
            files = Collections.emptyList();
        }

        System.out.println("update=====================================================");
        List<CommunityPhoto> savedCommunityPhotos = communityService.findAllCommunityPhotosFromCommunity(community);
        List<CommunityPhoto> deleteCommunityPhotos = new ArrayList<>();

        for (Long deleteCommunityPhotoId : dto.getDeleteCommunityPhotoIds()) {
            deleteCommunityPhotos.add(communityPhotoService.findById(community.getId(), deleteCommunityPhotoId));
        }

        photoService.validateCommunityPhotoCountExceeded(savedCommunityPhotos.size() - deleteCommunityPhotos.size()
                + files.size());

        communityService.modifyCommunity(member, dto, communityId, deleteCommunityPhotos);

        List<CommunityPhoto> communityPhotos = new ArrayList<>();
        communityPhotos.addAll(community.getCommunityPhotos());

        if (files.size() != 0)
            communityPhotos.addAll(communityService.saveCommunityPhotos(community, files));

        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(community, true, communityPhotos);
        result.setMyProfileImgUrl(member.getMemberPhoto().getPhotoUrl());

        return ResponseEntity.ok(result);
    }

    @ApiOperation("커뮤니티 게시글 삭제")
    @DeleteMapping("/{communityId}")
    public ResponseEntity<ResultDto> deleteCommunity(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long communityId) {
        Member member = memberService.findByMember(token);

        return ResponseEntity.ok(ResultDto
                .builder()
                .data(communityService.deleteCommunity(member,communityId))
                .build());
    }

    @ApiOperation("커뮤니티 좋아요")
    @PutMapping("/{communityId}/like")
    public ResponseEntity<ResultDto> saveCommunityLike(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long communityId) {
        Member member = memberService.findByMember(token);
        Community community = communityService.getCommunityById(communityId);

        communityLikedMemberService.save(member, community);
        fcmNotificationService.sendNotification(new FCMNotificationRequestDto(community.getMember().getId(), member.getNickname(), member.getId(), COMMUNITY_LIKE, communityId));

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("커뮤니티 좋아요 취소")
    @DeleteMapping("/{communityId}/like")
    public ResponseEntity<ResultDto> deleteCommunityLike(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long communityId) {
        Member member = memberService.findByMember(token);
        Community community = communityService.getCommunityById(communityId);

        communityLikedMemberService.delete(member, community);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("(사용 X) 시향기 -> 향BTI_시향기로 바꾸기")
    @PutMapping("/change")
    public ResponseEntity<?> changeCategory(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        communityService.changeCategory();

        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
