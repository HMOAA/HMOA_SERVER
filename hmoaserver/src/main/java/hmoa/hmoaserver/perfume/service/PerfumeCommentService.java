package hmoa.hmoaserver.perfume.service;


import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
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
    private final static String CREATE_LIKE_SUCCESS = "좋아요 등록 성공";
    private final static String DELETE_LIKE_SUCCESS = "좋아요 취소 성공";
    private Long deleteMemberId= 0l;
    private final MemberRepository memberRepository;
    private final PerfumeCommentRepository commentRepository;
    private final PerfumeCommentLikedRepository commentHeartRepository;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final PerfumeService perfumeService;

    public PerfumeComment commentSave(Member member, Long id, PerfumeCommentRequestDto dto){
        Perfume findPerfume = perfumeService.findById(id);
        return commentRepository.save(dto.toEntity(member, findPerfume));

    }

    public String saveLike(String token,Long commentId){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(null, COMMENT_NOT_FOUND));
        if (!hasLike(findComment,findMember)){
            findComment.increaseHeartCount();
            PerfumeCommentLiked heart = PerfumeCommentLiked.builder()
                    .member(findMember)
                    .perfumeComment(findComment)
                    .build();
            commentHeartRepository.save(heart);
            return CREATE_LIKE_SUCCESS;
        }
        throw new CustomException(null,DUPLICATE_LIKED);
    }
    public String deleteLike(String token,Long commentId){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(null, COMMENT_NOT_FOUND));
        PerfumeCommentLiked perfumeCommentLiked = commentHeartRepository.findByPerfumeCommentAndMember(findComment,findMember)
                .orElseThrow(()-> new CustomException(null,HEART_NOT_FOUND));
        commentHeartRepository.delete(perfumeCommentLiked);
        findComment.decreaseHeartCount();
        return DELETE_LIKE_SUCCESS;

    }

    public boolean hasLike(final PerfumeComment perfumeComment, final Member member){
        return commentHeartRepository.findByPerfumeCommentAndMember(perfumeComment, member).isPresent();
    }

    public PerfumeComment modifyComment(String token, Long commentId,String content){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(null, COMMENT_NOT_FOUND));
        if(findComment.getMember().getId() != findMember.getId()){
            throw new CustomException(null,UNAUTHORIZED_COMMENT);
        }
        findComment.modifyComment(content);
        return findComment;
    }

    /**
     * 비로그인시 댓글 조회
     */
    public PerfumeCommentGetResponseDto findCommentsByPerfume(Long perfumeId,int page){
        try{
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByCreatedAtDesc(perfumeId,PageRequest.of(page,10));
            Long commentCount = foundComments.getTotalElements();
            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(PerfumeCommentResponseDto::new).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount,dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     * 로그인 시 댓글 조회
     */
    public PerfumeCommentGetResponseDto findCommentsByPerfume(Long perfumeId, int page, Member member){
        try{
            Page<PerfumeComment> foundComments = commentRepository.findAllByPerfumeIdOrderByCreatedAtDesc(perfumeId,PageRequest.of(page,10));
            Long commentCount = foundComments.getTotalElements();
            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(comment -> {
                if(hasLike(comment,member)){
                    return new PerfumeCommentResponseDto(comment,true, member);
                }else{
                    return new PerfumeCommentResponseDto(comment,false, member);
                }
            }).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount,dto);
        }catch (DataAccessException | ConstraintViolationException e){
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     * 비 로그인시 댓글 조회 (좋아요순)
     */
    public PerfumeCommentGetResponseDto findTopCommentsByPerfume(Long perfumeId, int page, int size){
        try{
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByCreatedAtDesc(perfumeId,PageRequest.of(page,size));
            Long commentCount = foundComments.getTotalElements();
            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(PerfumeCommentResponseDto::new).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount,dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     * 로그인 시 댓글 조회 (좋아요순)
     */
    public PerfumeCommentGetResponseDto findTopCommentsByPerfume(Long perfumeId, int page, int size, Member member){
        try{
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByHeartCountDesc(perfumeId,PageRequest.of(page,size));
            Long commentCount = foundComments.getTotalElements();
            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(comment -> {
                if(hasLike(comment,member)){
                    return new PerfumeCommentResponseDto(comment,true, member);
                }else {
                    return new PerfumeCommentResponseDto(comment,false, member);
                }
            }).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount,dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }
    public void deleteComment(Member member, Long commentId) {
        PerfumeComment perfumeComment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(null,COMMENT_NOT_FOUND));
        if(member.getId() != perfumeComment.getMember().getId()) {
            throw new CustomException(null, FORBIDDEN_AUTHORIZATION);
        }
        commentRepository.delete(perfumeComment);
    }

    public void deleteMemberComment(Member member){
        Member deleteMember = memberRepository.findById(deleteMemberId).get();
        List<PerfumeComment> comments = commentRepository.findAllByMemberId(member.getId());
        for(PerfumeComment comment : comments){
            comment.modifyCommentMember(deleteMember);
        }
    }
}
