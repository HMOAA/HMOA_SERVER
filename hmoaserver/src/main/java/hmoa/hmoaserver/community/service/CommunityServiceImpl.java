package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityModifyRequestDto;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityServiceImpl implements CommunityService {
    private final static String DELETE_SUCCESS = "삭제 성공";
    private final CommunityRepository communityRepository;

    @Override
    @Transactional
    public Community saveCommunity(Member member, CommunityDefaultRequestDto communityDefaultRequestDto) {
        try {
            return communityRepository.save(communityDefaultRequestDto.toEntity(member));
        }catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    @Override
    public Page<Community> getAllCommunitysByCategory(int page, Category category) {
        return communityRepository.findAllByCategory(category, PageRequest.of(page,10));
    }

    @Override
    public Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(()->new CustomException(null,Code.COMMUNITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public Community modifyCommunity(Member member, CommunityModifyRequestDto communityModifyRequestDto,Long communityId) {
        Community community = getCommunityById(communityId);
        if(!community.isWrited(member)){
            throw new CustomException(null,Code.FORBIDDEN_AUTHORIZATION);
        }
        community.modifyContent(communityModifyRequestDto.getContent());
        community.modifyTitle(communityModifyRequestDto.getTitle());
        return community;
    }

    @Override
    @Transactional
    public String deleteCommunity(Member member, Long communityId) {
        Community community = getCommunityById(communityId);
        if(!community.isWrited(member)){
            throw new CustomException(null,Code.FORBIDDEN_AUTHORIZATION);
        }
        communityRepository.delete(community);
        return DELETE_SUCCESS;
    }


}
