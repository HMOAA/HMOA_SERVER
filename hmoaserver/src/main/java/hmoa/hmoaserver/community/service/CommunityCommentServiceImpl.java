package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentAllResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultResponseDto;
import hmoa.hmoaserver.community.repository.CommunityCommentRepository;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityCommentServiceImpl implements CommunityCommentService{
    private final CommunityCommentRepository commentRepository;

    @Override
    public Page<CommunityComment> getCommunityComment(Long communityId, int pageNum) {
        return commentRepository.findAllByCommunityId(communityId, PageRequest.of(pageNum, 6));
    }

    @Override
    public List<CommunityCommentDefaultResponseDto> getCommunityComment(Long communityId, int pageNum, Member member) {
        Page<CommunityComment> comments = commentRepository.findAllByCommunityId(communityId,PageRequest.of(pageNum,6));
        return comments.stream().map(comment -> {
            if(isWritedComment(member,comment)) return new CommunityCommentDefaultResponseDto(comment,true);
            return new CommunityCommentDefaultResponseDto(comment,false);
        }).collect(Collectors.toList());
    }


    @Override
    public CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto,Community community) {
        return commentRepository.save(dto.toEntity(member,community));
    }

    private boolean isWritedComment(Member member, CommunityComment comment){
        return comment.getMember().getId().equals(member.getId());
    }

}
