package hmoa.hmoaserver.perfume.service;


import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.fcm.service.constant.NotificationType;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentGetResponseDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentRequestDto;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentResponseDto;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentLikedRepository;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PerfumeCommentService {
    @Value("${default.profile}")
    private String DEFALUT_PROFILE_URL;
    private static final String CREATE_LIKE_SUCCESS = "좋아요 등록 성공";
    private static final String DELETE_LIKE_SUCCESS = "좋아요 취소 성공";
    private static final PageRequest pageRequest = PageRequest.of(0, 10);
    private final PerfumeCommentRepository commentRepository;
    private final PerfumeCommentLikedRepository commentHeartRepository;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final PerfumeService perfumeService;
    private final FCMNotificationService fcmNotificationService;

    public PerfumeComment commentSave(Member member, Long id, PerfumeCommentRequestDto dto) {
        Perfume findPerfume = perfumeService.findById(id);
        return commentRepository.save(dto.toEntity(member, findPerfume));

    }

    public Page<PerfumeComment> findPerfumeCommentByMember(Member member, int page) {
        return commentRepository.findAllByMember(member, PageRequest.of(page, PageSize.TEN_SIZE.getSize()));
    }

    public Page<PerfumeComment> findPerfumeCommentByMemberAndCursor(Member member, Long cursor) {
        return commentRepository.findPerfumeCommentByMemberAndNextCursor(member, cursor, pageRequest);
    }

    public String saveLike(String token, Long commentId) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(null, COMMENT_NOT_FOUND));

        if (!hasLike(findComment, findMember)) {
            findComment.increaseHeartCount();
            PerfumeCommentLiked heart = PerfumeCommentLiked.builder()
                    .member(findMember)
                    .perfumeComment(findComment)
                    .build();
            commentHeartRepository.save(heart);
            fcmNotificationService.sendNotification(new FCMNotificationRequestDto(findComment.getMember().getId(), findMember.getNickname(), findMember.getId(), NotificationType.PERFUME_COMMENT_LIKE, findComment.getPerfume().getId()));
            return CREATE_LIKE_SUCCESS;
        }

        throw new CustomException(null, DUPLICATE_LIKED);
    }

    public String deleteLike(String token, Long commentId) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(null, COMMENT_NOT_FOUND));
        PerfumeCommentLiked perfumeCommentLiked = commentHeartRepository.findByPerfumeCommentAndMember(findComment,findMember)
                .orElseThrow(() -> new CustomException(null,HEART_NOT_FOUND));
        commentHeartRepository.delete(perfumeCommentLiked);
        findComment.decreaseHeartCount();
        return DELETE_LIKE_SUCCESS;
    }

    public boolean hasLike(final PerfumeComment perfumeComment, final Member member) {
        return commentHeartRepository.findByPerfumeCommentAndMember(perfumeComment, member).isPresent();
    }

    public PerfumeComment modifyComment(String token, Long commentId,String content) {
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(null, COMMENT_NOT_FOUND));
        if (findComment.getMember().getId() != findMember.getId()) {
            throw new CustomException(null, UNAUTHORIZED_COMMENT);
        }
        findComment.modifyComment(content);
        return findComment;
    }

    /**
     * 비로그인시 댓글 조회
     */
    public PerfumeCommentGetResponseDto findCommentsByPerfume(Long perfumeId, int page) {
        try {
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByCreatedAtDescIdDesc(perfumeId, PageRequest.of(page,10));
            boolean isLastPage = PageUtil.isLastPage(foundComments);
            Long commentCount = foundComments.getTotalElements();

            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(PerfumeCommentResponseDto::new).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount, isLastPage, dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public Page<PerfumeComment> findCommentsByPerfume(Long perfumeId, Long cursor) {
        if (PageUtil.isFistCursor(cursor)) {
            return commentRepository.findAllByPerfumeIdOrderByCreatedAtDescIdDesc(perfumeId, pageRequest);
        }
        return commentRepository.findPerfumeCommentOrderByCreatedAtNextCursor(perfumeId, cursor, pageRequest);
    }

    public Long totalCountsByPerfume(Long perfumeId) {
        return commentRepository.countByPerfumeId(perfumeId);
    }

    /**
     * 로그인 시 댓글 조회
     */
    public PerfumeCommentGetResponseDto findCommentsByPerfume(Long perfumeId, int page, Member member) {
        try {
            Page<PerfumeComment> foundComments = commentRepository.findAllByPerfumeIdOrderByCreatedAtDescIdDesc(perfumeId,PageRequest.of(page,10));
            boolean isLastPage = PageUtil.isLastPage(foundComments);
            Long commentCount = foundComments.getTotalElements();

            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(comment -> {
                if (hasLike(comment, member)) {
                    return new PerfumeCommentResponseDto(comment,true, member);
                }
                return new PerfumeCommentResponseDto(comment,false, member);
            }).collect(Collectors.toList());

            return new PerfumeCommentGetResponseDto(commentCount, isLastPage, dto);
        }catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     * 비 로그인시 댓글 조회 (좋아요순)
     */
    public PerfumeCommentGetResponseDto findTopCommentsByPerfume(Long perfumeId, int page, int size) {
        try {
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByCreatedAtDescIdDesc(perfumeId,PageRequest.of(page, size));
            boolean isLastPage = PageUtil.isLastPage(foundComments);
            Long commentCount = foundComments.getTotalElements();

            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(PerfumeCommentResponseDto::new).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount, isLastPage, dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     * 로그인 시 댓글 조회 (좋아요순)
     */
    public PerfumeCommentGetResponseDto findTopCommentsByPerfume(Long perfumeId, int page, int size, Member member){
        try {
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByHeartCountDescIdAsc(perfumeId,PageRequest.of(page,size));
            boolean isLastPage = PageUtil.isLastPage(foundComments);
            Long commentCount = foundComments.getTotalElements();

            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(comment -> {
                if (hasLike(comment, member)) {
                    return new PerfumeCommentResponseDto(comment,true, member);
                } else {
                    return new PerfumeCommentResponseDto(comment,false, member);
                }
            }).collect(Collectors.toList());

            return new PerfumeCommentGetResponseDto(commentCount, isLastPage, dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public PerfumeComment findOnePerfumeComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(null, COMMENT_NOT_FOUND));
    }

    public void deleteComment(Member member, Long commentId) {
        PerfumeComment perfumeComment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(null,COMMENT_NOT_FOUND));
        if(member.getId() != perfumeComment.getMember().getId()) {
            throw new CustomException(null, FORBIDDEN_AUTHORIZATION);
        }
        commentRepository.delete(perfumeComment);
    }

    public boolean isPerfumeCommentLiked(PerfumeComment comment, Member member) {
        return commentHeartRepository.findByPerfumeCommentAndMember(comment, member).isPresent();
    }
}
