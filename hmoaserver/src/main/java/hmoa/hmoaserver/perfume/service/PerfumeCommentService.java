package hmoa.hmoaserver.perfume.service;


import hmoa.hmoaserver.exception.CustomException;
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

    public PerfumeCommentGetResponseDto getComments(Long perfumeId,int page,int sortType){
        Perfume perfume = perfumeService.findById(perfumeId);
        Pageable pageable = PageRequest.of(page,10);
        Page<PerfumeComment> comments = commentRepository.findAllByPerfumeId(perfumeId,pageable);
        Long commentCount=comments.getTotalElements();
        List<PerfumeCommentResponseDto> commentsDto = comments.stream().map(comment -> new PerfumeCommentResponseDto(comment)).collect(Collectors.toList());
        return new PerfumeCommentGetResponseDto(commentCount,commentsDto);
    }
}
