package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityModifyRequestDto;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        return communityRepository.findAllByCategoryOrderByCreatedAtDesc(category, PageRequest.of(page,10));
    }

    @Override
    public Page<Community> getCommunityByHome() {
        return communityRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 5));
    }

    @Override
    public Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(()->new CustomException(null,Code.COMMUNITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public Community modifyCommunity(Member member, CommunityModifyRequestDto communityModifyRequestDto,Long communityId,
                                     List<CommunityPhoto> deleteCommunityPhotos) {
        Community community = getCommunityById(communityId);

        if(!community.isWrited(member)){
            throw new CustomException(null,Code.FORBIDDEN_AUTHORIZATION);
        }

        community.modifyContent(communityModifyRequestDto.getContent());
        community.modifyTitle(communityModifyRequestDto.getTitle());

        for (CommunityPhoto deleteCommunityPhoto : deleteCommunityPhotos) {
            deleteCommunityPhoto.delete();
        }

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

    @Override
    @Transactional
    public List<CommunityPhoto> findAllCommunityPhotosFromCommunity(Community community) {
        return community.getCommunityPhotos();
    }
}
