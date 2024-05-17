package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentLikedRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeCommentLikedMemberService {
    private final PerfumeCommentLikedRepository commentLikedRepository;

    public boolean isMemberLikedPerfumeComment(Member member, PerfumeComment perfumeComment) {
        try {
            return commentLikedRepository.findByPerfumeCommentAndMember(perfumeComment, member).isPresent();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public PerfumeCommentLiked save(PerfumeComment perfumeComment, Member member) {
        try {
            perfumeComment.increaseHeartCount();
            PerfumeCommentLiked commentLiked = PerfumeCommentLiked.builder()
                    .perfumeComment(perfumeComment)
                    .member(member)
                    .build();

            return commentLikedRepository.save(commentLiked);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public void delete(PerfumeComment perfumeComment, Member member) {
        try {
            PerfumeCommentLiked commentLiked = findOnePerfumeCommentLiked(perfumeComment, member);
            perfumeComment.decreaseHeartCount();

            commentLikedRepository.delete(commentLiked);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public PerfumeCommentLiked findOnePerfumeCommentLiked(PerfumeComment perfumeComment, Member member) {
        return commentLikedRepository.findByPerfumeCommentAndMember(perfumeComment, member).orElseThrow(() ->
                new CustomException(null, SERVER_ERROR));
    }

    @Transactional(readOnly = true)
    public Page<PerfumeCommentLiked> findAllByMember(Member member, int page) {
        return commentLikedRepository.findAllByMemberOrderByIdDesc(member, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    @Transactional(readOnly = true)
    public Page<PerfumeCommentLiked> findAllByMemberAndCursor(Member member, Long cursor) {
        return commentLikedRepository.findAllByMemberNextCursor(member, cursor, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize()));
    }
}
