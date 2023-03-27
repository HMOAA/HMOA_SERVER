package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.dto.TokenResponseDto;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.oauth.jwt.Token;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtService jwtService;

    @Transactional
    public Member save(Member member){
        try{
            return memberRepository.save(member);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public Token reIssue(String rememberedToken){
        if(!(jwtService.isTokenValid(rememberedToken)== JwtResultType.VALID_JWT)){
            throw new CustomException(null, WRONG_TYPE_TOKEN);
        }
        if (memberRepository.findByRefreshToken(rememberedToken).isPresent()){
            Member member=memberRepository.findByRefreshToken(rememberedToken).get();
            String accessToken=jwtService.createAccessToken(member.getEmail(),member.getRole());
            String refreshToken=jwtService.createRefreshToken(member.getEmail(),member.getRole());
            jwtService.updateRefreshToken(member.getEmail(),refreshToken);
            return new Token(accessToken,refreshToken);
        }else{
            log.info("일치하는 회원이 없습니다.");
            throw new CustomException(null, MEMBER_NOT_FOUND);
        }

    }

    @Transactional
    public void updateRole(Member member){
        member.authorizeUser();
        save(member);
    }
    /**
     * 닉네임 중복 검사
     */
    public boolean isExistingNickname(String nickname){
        Boolean exisitingNickname = false;
        try{
            exisitingNickname=memberRepository.existsByNickname(nickname);
        }catch (RuntimeException e){
            throw new CustomException(e,SERVER_ERROR);
        }
        if(exisitingNickname){
            throw new CustomException(null, DUPLICATE_NICKNAME);
        }
        return exisitingNickname;
    }
    /**
     * 회원 하나 조회
     */
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(null, MEMBER_NOT_FOUND));
    }

    /**
     * 첫 로그인시 회원 업데이트
     */
    @Transactional
    public void joinMember(Member member, int age,String sex,String nickname){
        try{
            member.updateAge(age);
            member.updateSex(sex);
            member.updateNickname(nickname);
        }catch (RuntimeException e){
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 닉네임 업데이트
     */
    @Transactional
    public void updateNickname(Member member, String nickname){
        isExistingNickname(nickname);
        member.updateNickname(nickname);
        save(member);
    }

    /**
     * 나이 업데이트
     */
    @Transactional
    public void updateAge(Member member, int age){
        member.updateAge(age);
        save(member);
    }
}
