package hmoa.hmoaserver.perfume.service;


import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentHeart;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentRequestDto;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentHeartRepository;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentRepository;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PerfumeCommentService {
    private final static String CREATE_HEART_SUCCESS = "좋아요 등록 성공";
    private final static String DELETE_HEART_SUCCESS = "좋아요 취소 성공";
    private final PerfumeCommentRepository commentRepository;
    private final PerfumeCommentHeartRepository commentHeartRepository;
    private final JwtService jwtService;

    private final MemberService memberService;

    private final PerfumeService perfumeService;

    @Transactional
    public PerfumeComment commentSave(String token,Long id, PerfumeCommentRequestDto dto){
        String email=jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        Perfume findPerfume = perfumeService.findById(id);
        return commentRepository.save(dto.toEntity(findMember,findPerfume));

    }

    @Transactional
    public String updateHeart(String token,Long commentId){
        String email = jwtService.getEmail(token);
        Member findMember = memberService.findByEmail(email);
        PerfumeComment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(null, COMMENT_NOT_FOUND));
        log.info("1");
        if (!hasHeart(findComment,findMember)){
            findComment.increaseHeartCount();
            log.info("2");
            PerfumeCommentHeart heart = PerfumeCommentHeart.builder()
                    .member(findMember)
                    .perfumeComment(findComment)
                    .build();
            commentHeartRepository.save(heart);
            log.info("3");
            return CREATE_HEART_SUCCESS;
        }
        log.info("4");
        findComment.decreaseHeartCount();
        return removeHeart(findComment,findMember);
    }
    private String removeHeart(final PerfumeComment perfumeComment, final Member member){
        PerfumeCommentHeart perfumeCommentHeart = commentHeartRepository.findByPerfumeCommentAndMember(perfumeComment,member)
                .orElseThrow(()-> new CustomException(null,HEART_NOT_FOUND));
        commentHeartRepository.delete(perfumeCommentHeart);
        return DELETE_HEART_SUCCESS;

    }

    public boolean hasHeart(final PerfumeComment perfumeComment, final Member member){
        return commentHeartRepository.findByPerfumeCommentAndMember(perfumeComment, member).isPresent();
    }
}
