package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;

    @Override
    public Page<Community> getAllCommunitysByCategory(int page, Category category) {
        return communityRepository.findAllByCategory(category, PageRequest.of(page,10));
    }

    @Override
    public Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(()->new CustomException(null,Code.COMMUNITY_NOT_FOUND));
    }

    @Override
    public Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto) {
        return communityRepository.save(communityDefaultRequestDto.toEntity(member));
    }


}
