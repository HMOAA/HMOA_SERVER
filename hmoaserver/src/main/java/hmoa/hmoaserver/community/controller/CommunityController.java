package hmoa.hmoaserver.community.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.*;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import hmoa.hmoaserver.photo.service.CommunityPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @ApiOperation(value = "사진 저장 test")
    @PostMapping(value = "/photosSave", consumes = "multipart/form-data")
    public void savePhoto(@RequestPart(value = "image", required = false) List<MultipartFile> files) {
        System.out.println(files.size());
    }

    @ApiOperation(value = "단일 사진 저장 test")
    @PostMapping(value = "/photoSave")
    public void savePhoto(@RequestPart(value = "file") MultipartFile file) {
        System.out.println(file.getSize());
    }

    @ApiOperation("게시글 저장")
    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<CommunityDefaultResponseDto> saveCommunity(@RequestPart(value="image", required = false) List<MultipartFile> files, @RequestHeader("X-AUTH-TOKEN") String token, CommunityDefaultRequestDto dto){
        Member member = memberService.findByMember(token);
        Community community = communityService.saveCommunity(member, dto);

        System.out.println("*********************************************************");
        if(files != null) {
            System.out.println("=========================================" + files.size());
            photoService.validateCommunityPhotoCountExceeded(files.size());
            communityPhotoService.saveCommunityPhotos(community, files);
        }

        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(community);
        result.setWrited(true);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("카테고리 별 게시글 조회")
    @GetMapping("/category")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findAllCommunity(@RequestParam Category category,@RequestParam int page) {
        Page<Community> communities = communityService.getAllCommunitysByCategory(page,category);
        List<CommunityByCategoryResponseDto> result = communities.stream().map(community -> new CommunityByCategoryResponseDto(community)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @ApiOperation("커뮤니티 홈 조회")
    @GetMapping("/home")
    public ResponseEntity<List<CommunityByCategoryResponseDto>> findCommunitByHome() {
        Page<Community> communities = communityService.getCommunityByHome();
        List<CommunityByCategoryResponseDto> result = communities.stream().map(CommunityByCategoryResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @ApiOperation("커뮤니티 게시글 단건 조회")
    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDefaultResponseDto> findOneCommunity(@RequestHeader(value = "X-AUTH-TOKEN",required = false) String token,@PathVariable Long communityId){
        Community community = communityService.getCommunityById(communityId);
        CommunityDefaultResponseDto result = new CommunityDefaultResponseDto(community);
        if(memberService.isTokenNullOrEmpty(token)){
            return ResponseEntity.ok(result);
        }
        Member member = memberService.findByMember(token);
        if(member == community.getMember()){
            result.setWrited(true);
        }
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

        communityService.modifyCommunity(member,dto,communityId,deleteCommunityPhotos);

        if (files.size() != 0)
            communityPhotoService.saveCommunityPhotos(community, files);

        return ResponseEntity.ok(new CommunityDefaultResponseDto(community,true));
    }

    @ApiOperation("커뮤니티 게시글 삭제")
    @DeleteMapping("/{communityId}")
    public ResponseEntity<ResultDto> deleteCommunity(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long communityId){
        Member member = memberService.findByMember(token);
        return ResponseEntity.ok(ResultDto
                .builder()
                .data(communityService.deleteCommunity(member,communityId))
                .build());
    }
}
