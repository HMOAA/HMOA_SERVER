package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.repository.CommunityCommentRepository;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityCommentServiceImpl implements CommunityCommentService{
    private final CommunityCommentRepository commentRepository;

    @Override
    public Page<CommunityComment> getCommunityComment(Long communityId, int pageNum) {
        return commentRepository.findAllByCommunityId(communityId, PageRequest.of(pageNum, 8));
    }

    @Override
    public CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto,Community community) {
        return commentRepository.save(dto.toEntity(member,community));
    }
}
