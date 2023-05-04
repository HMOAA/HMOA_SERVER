package hmoa.hmoaserver.perfume.service;


import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentRequestDto;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentRepository;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PerfumeCommentService {
    private final PerfumeCommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;
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
}
