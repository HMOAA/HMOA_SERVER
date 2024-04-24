package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityModifyRequestDto;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import hmoa.hmoaserver.photo.service.CommunityPhotoService;
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
    private static final String DELETE_SUCCESS = "삭제 성공";
    private static final PageRequest pageRequest = PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize());
    private final CommunityRepository communityRepository;
    private final CommunityPhotoService communityPhotoService;

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
        return communityRepository.findAllByCategoryOrderByCreatedAtDescIdDesc(category, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    @Override
    public Page<Community> getAllCommunitysByCategory(Long cursor, Category category) {
        if (isFirstCursor(cursor)) {
            return communityRepository.findAllByCategoryOrderByCreatedAtDescIdDesc(category, pageRequest);
        }
        return communityRepository.findCommunityNextPage(cursor, category, pageRequest);
    }

    @Override
    public Page<Community> getTopCommunitysByCategory(int page, Category category) {
        return communityRepository.findAllByCategoryOrderByHeartCountDescIdDesc(category, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    @Override
    public Page<Community> getCommunityByHome() {
        return communityRepository.findAllByOrderByCreatedAtDescIdAsc(pageRequest);
    }

    @Override
    public Page<Community> getCommunityByMember(Member member, int page) {
        return communityRepository.findAllByMemberOrderByCreatedAtDescIdDesc(member, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    @Override
    public Page<Community> getCommunityByMemberAndCursor(Member member, Long cursor) {
        return communityRepository.findCommunityNextPageByMember(member, cursor, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize()));
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
    public List<CommunityPhoto> saveCommunityPhotos(Community community, List<MultipartFile> files) {
        return communityPhotoService.saveCommunityPhotos(community, files);
    }

    @Override
    @Transactional
    public String deleteCommunity(Member member, Long communityId) {
        Community community = getCommunityById(communityId);
        if(!community.isWrited(member)){
            throw new CustomException(null,Code.FORBIDDEN_AUTHORIZATION);
        }
        List<CommunityComment> comments = community.getCommunityComments();
        comments.forEach(CommunityComment::setCommunityIsNull);
        communityRepository.delete(community);
        return DELETE_SUCCESS;
    }

    @Override
    @Transactional
    public List<CommunityPhoto> findAllCommunityPhotosFromCommunity(Community community) {
        return community.getCommunityPhotos();
    }

    private static boolean isFirstCursor(Long cursor) {
        return cursor == 0;
    }
}
