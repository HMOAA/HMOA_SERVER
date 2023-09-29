package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentAllResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentModifyRequestDto;
import hmoa.hmoaserver.community.repository.CommunityCommentRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityCommentServiceImpl implements CommunityCommentService{
    private final static String DELETE_COMMENT = "답변 삭제 성공";
    private final CommunityCommentRepository commentRepository;

    @Override
    public CommunityComment findOneComunityComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->new CustomException(null, Code.COMMENT_NOT_FOUND));
    }

    @Override
    public Page<CommunityComment> findAllCommunityComment(Long communityId, int pageNum) {
        return commentRepository.findAllByCommunityId(communityId, PageRequest.of(pageNum, 6));
    }


    @Override
    @Transactional
    public CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto,Community community) {
        return commentRepository.save(dto.toEntity(member,community));
    }

    @Override
    @Transactional
    public CommunityComment modifyCommunityComment(Member member, CommunityCommentModifyRequestDto dto, Long commentId) {
        CommunityComment comment = findOneComunityComment(commentId);
        if(!comment.isWrited(member)){
            throw new CustomException(null,Code.FORBIDDEN_AUTHORIZATION);
        }
        comment.modifyComment(dto.getContent());
        return comment;
    }

    @Override
    @Transactional
    public String deleteCommunityComment(Member member, Long commentId) {
        CommunityComment comment = findOneComunityComment(commentId);
        if(!comment.isWrited(member)){
            throw new CustomException(null,Code.FORBIDDEN_AUTHORIZATION);
        }
        commentRepository.delete(comment);
        return DELETE_COMMENT;
    }

}
