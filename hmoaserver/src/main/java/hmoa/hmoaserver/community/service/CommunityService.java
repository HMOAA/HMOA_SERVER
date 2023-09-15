package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;


public interface CommunityService {
    Page<Community> getAllCommunitysByCategory(int page);
    Community getCommunityById(Long id);

    Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto);

}
