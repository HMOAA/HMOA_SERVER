package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;

    @Override
    public Page<Community> getAllCommunitysByCategory(int page) {
        return null;
    }

    @Override
    public Community getCommunityById(Long id) {
        return null;
    }

    @Override
    public Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto) {
        return communityRepository.save(communityDefaultRequestDto.toEntity(member));
    }


}
