package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityModifyRequestDto;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;


public interface CommunityService {
    Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto);

    Page<Community> getAllCommunitysByCategory(int page, Category category);
    Community getCommunityById(Long communityId);

    Community modifyCommunity(Member member, CommunityModifyRequestDto communityModifyRequestDto, Long communityId);

    String deleteCommunity(Member member, Long communityId);

}
