package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityModifyRequestDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CommunityService {
    Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto);
    Page<Community> getAllCommunitysByCategory(int page, Category category);
    Page<Community> getAllCommunitysByCategory(Long cursor, Category category);
    Page<Community> getTopCommunitysByCategory(int page, Category category);
    Page<Community> getCommunityByHome();
    Page<Community> getCommunityByMember(Member member, int page);
    Page<Community> getCommunityByMemberAndCursor(Member member, Long cursor);
    Community getCommunityById(Long communityId);
    Community modifyCommunity(Member member, CommunityModifyRequestDto communityModifyRequestDto, Long communityId, List<CommunityPhoto> deleteCommunityPhotos);
    List<CommunityPhoto> saveCommunityPhotos(Community community, List<MultipartFile> files);
    String deleteCommunity(Member member, Long communityId);
    List<CommunityPhoto> findAllCommunityPhotosFromCommunity(Community community);
    void changeCategory();
}
