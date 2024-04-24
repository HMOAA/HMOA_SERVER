package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
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
    private static final PageRequest pageReqeust = PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.SIX_SIZE.getSize());
    private final CommunityCommentRepository commentRepository;

    @Override
    public CommunityComment findOneComunityComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->new CustomException(null, Code.COMMENT_NOT_FOUND));
    }

    @Override
    public Page<CommunityComment> findAllCommunityComment(Long communityId, int pageNum) {
        return commentRepository.findAllByCommunityIdOrderByCreatedAtDescIdDesc(communityId, PageRequest.of(pageNum, PageSize.SIX_SIZE.getSize()));
    }

    @Override
    public Page<CommunityComment> findAllCommunityComment(Long communityId, Long cursor) {
        if (PageUtil.isFistCursor(cursor)) {
            return commentRepository.findAllByCommunityIdOrderByCreatedAtDescIdDesc(communityId, pageReqeust);
        }
        return commentRepository.findCommunityCommentNextPage(communityId, cursor, pageReqeust);
    }

    @Override
    public Page<CommunityComment> findAllCommunityCommentByMember(Member member, int page) {
        return commentRepository.findAllByMemberOrderByCreatedAtDescIdDesc(member, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    @Override
    public Page<CommunityComment> findAllByMemberNextCursor(Member member, Long cursor) {
        return commentRepository.findCommunityCommentByMemberNextPage(member, cursor, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize()));
    }

    @Override
    public Long countAllCommunityComment(Long communityId) {
        return commentRepository.countByCommunityId(communityId);
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
}
