package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityLikedMember;
import hmoa.hmoaserver.community.repository.CommunityLikedMemberRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityLikedMemberServiceImpl implements CommunityLikedMemberService{
    private final CommunityLikedMemberRepository communityLikedMemberRepository;

    @Override
    public boolean isCommunityLikedMember(Member member, Community community) {
        return communityLikedMemberRepository.findByMemberAndCommunity(member, community).isPresent();
    }

    @Override
    public CommunityLikedMember save(Member member, Community community) {
        if (isCommunityLikedMember(member, community)) {
            throw new CustomException(null, DUPLICATE_LIKED);
        }

        try {
            CommunityLikedMember communityLikedMember = CommunityLikedMember.builder()
                    .community(community)
                    .member(member)
                    .build();

            community.increaseHeartCount();
            return communityLikedMemberRepository.save(communityLikedMember);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

    }

    @Override
    public void delete(Member member, Community community) {
        CommunityLikedMember communityLikedMember = findOneCommunityLiked(member, community);

        try {
            communityLikedMemberRepository.delete(communityLikedMember);
            community.decreaseHeartCount();
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @Override
    public CommunityLikedMember findOneCommunityLiked(Member member, Community community) {
        return communityLikedMemberRepository.findByMemberAndCommunity(member, community)
                .orElseThrow(() -> new CustomException(null, COMMUNITYLIKEDMEMEBER_NOT_FOUND));
    }

}
