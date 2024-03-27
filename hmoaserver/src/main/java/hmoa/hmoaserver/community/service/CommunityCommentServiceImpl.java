package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentModifyRequestDto;
import hmoa.hmoaserver.community.repository.CommunityCommentRepository;
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
public class CommunityCommentServiceImpl implements CommunityCommentService{
    private static final String DELETE_COMMENT = "답변 삭제 성공";
    private static final PageRequest pageReqeust = PageRequest.of(0, 10);
    private final CommunityCommentRepository commentRepository;

    @Override
    public CommunityComment findOneComunityComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->new CustomException(null, Code.COMMENT_NOT_FOUND));
    }

    @Override
    public Page<CommunityComment> findAllCommunityComment(Long communityId, int pageNum) {
        return commentRepository.findAllByCommunityIdOrderByCreatedAtDescIdAsc(communityId, PageRequest.of(pageNum, 6));
    }

    @Override
    public Page<CommunityComment> findAllCommunityComment(Long communityId, Long cursor) {
        if (isFirstCursor(cursor)) {
            return commentRepository.findAllByCommunityIdOrderByCreatedAtDescIdAsc(communityId, pageReqeust);
        }
        return commentRepository.findCommunityCommentNextPage(communityId, cursor, pageReqeust);
    }


    @Override
    @Transactional
    public CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto, Community community) {
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

    private static boolean isFirstCursor(Long cursor) {
        return cursor == 0;
    }
}
