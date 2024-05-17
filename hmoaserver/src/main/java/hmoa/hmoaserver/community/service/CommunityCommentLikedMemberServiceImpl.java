package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.community.repository.CommunityCommentLikedMemberRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityCommentLikedMemberServiceImpl implements CommunityCommentLikedMemberService{
    private final CommunityCommentLikedMemberRepository commentLikedMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isCommentLikedMember(Member member, CommunityComment comment) {
        return commentLikedMemberRepository.findByMemberAndCommunityComment(member, comment).isPresent();
    }

    @Override
    public CommunityCommentLikedMember save(Member member, CommunityComment comment) {
        if (isCommentLikedMember(member, comment)) {
            throw new CustomException(null, DUPLICATE_LIKED);
        }

        try {
            CommunityCommentLikedMember communityCommentLikedMember = CommunityCommentLikedMember.builder()
                    .comment(comment)
                    .member(member)
                    .build();
            comment.increaseHeartCount();

            return commentLikedMemberRepository.save(communityCommentLikedMember);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

    }

    @Override
    public void delete(Member member, CommunityComment comment) {
        CommunityCommentLikedMember commentLikedMember = findOneCommentLiked(member, comment);

        try {
            commentLikedMemberRepository.delete(commentLikedMember);
            comment.decreaseHeartCount();
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

    }

    @Override
    public CommunityCommentLikedMember findOneCommentLiked(Member member, CommunityComment comment) {
        return commentLikedMemberRepository.findByMemberAndCommunityComment(member, comment)
                .orElseThrow(() -> new CustomException(null, COMMUNITYCOMMENTLIKEDMEMBER_NOT_FOUND));
    }

    @Override
    public Page<CommunityCommentLikedMember> findAllByMember(Member member, int page) {
        return commentLikedMemberRepository.findAllByMemberOrderByIdDesc(member, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    @Override
    public Page<CommunityCommentLikedMember> findAllByMemberAndCursor(Member member, Long cursor) {
        return commentLikedMemberRepository.findAllByMemberNextCursor(member, cursor, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize()));
    }
}
