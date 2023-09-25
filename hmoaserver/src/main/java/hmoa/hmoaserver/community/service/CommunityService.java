package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;


public interface CommunityService {
    Page<Community> getAllCommunitysByCategory(int page, Category category);
    Community getCommunityById(Long communityId);

    Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto);

}