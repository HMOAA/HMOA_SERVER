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
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final static String CREATE_LIKE_SUCCESS = "좋아요 등록 성공";
    private final static String DELETE_LIKE_SUCCESS = "좋아요 취소 성공";
    private Long deleteMemberId= 0l;
    private final MemberRepository memberRepository;
    private final PerfumeCommentRepository commentRepository;
    private final PerfumeCommentLikedRepository commentHeartRepository;
    private final JwtService jwtService;

    private final MemberService memberService;

    private final PerfumeService perfumeService;

    public PerfumeComment commentSave(String token,Long id, PerfumeCommentRequestDto dto){
        String email=jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        Perfume findPerfume = perfumeService.findById(id);
        return commentRepository.save(dto.toEntity(findMember,findPerfume));

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

    public String modifyComment(String token, Long commentId,String content){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(null, COMMENT_NOT_FOUND));
        if(findComment.getMember().getId() != findMember.getId()){
            throw new CustomException(null,UNAUTHORIZED_COMMENT);
        }
        findComment.modifyComment(content);
        return "MODIFY_SUCCESS";
    }

    public PerfumeCommentGetResponseDto findCommentsByPerfume(Long perfumeId,int page){
        try{
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByCreatedAtDesc(perfumeId,PageRequest.of(page,10));
            Long commentCount = foundComments.getTotalElements();
            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(comment -> new PerfumeCommentResponseDto(comment)).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount,dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public PerfumeCommentGetResponseDto findTopCommentsByPerfume(Long perfumeId,int page){
        try{
            Page<PerfumeComment> foundComments =
                    commentRepository.findAllByPerfumeIdOrderByHeartCountDesc(perfumeId,PageRequest.of(page,10));
            Long commentCount = foundComments.getTotalElements();
            List<PerfumeCommentResponseDto> dto = foundComments.stream().map(comment -> new PerfumeCommentResponseDto(comment)).collect(Collectors.toList());
            return new PerfumeCommentGetResponseDto(commentCount,dto);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public void deleteMemberComment(Member member){
        Member deleteMember = memberRepository.findById(deleteMemberId).get();
        List<PerfumeComment> comments = commentRepository.findAllByMemberId(member.getId());
        log.info("{}",comments.get(0).getMember().getId());
        for(PerfumeComment comment : comments){
            comment.modifyCommentMember(deleteMember);
        }
        log.info("{}",comments.get(0).getMember().getId());
    }
}
