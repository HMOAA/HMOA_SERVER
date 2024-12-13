package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.*;
import hmoa.hmoaserver.member.dto.MemberLoginResponseDto;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.oauth.jwt.Token;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.oauth.service.ProviderService;
import hmoa.hmoaserver.oauth.userinfo.OAuth2UserDto;
import hmoa.hmoaserver.photo.service.MemberPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static hmoa.hmoaserver.exception.Code.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final ProviderService providerService;
    private final MemberPhotoService memberPhotoService;

    @Transactional
    public Member save(Member member){
        try{
            return memberRepository.save(member);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void delete(Member member){
        try {
            memberRepository.delete(member);
        }catch (RuntimeException e){
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    @Transactional
    public Token reissueTokens(String rememberedToken){
        if (!(jwtService.isTokenValid(rememberedToken)== JwtResultType.VALID_JWT)) {
            throw new CustomException(null, WRONG_TYPE_TOKEN);
        }

        if (memberRepository.findByRefreshToken(rememberedToken).isPresent()) {
            Member member=memberRepository.findByRefreshToken(rememberedToken).get();
            String accessToken=jwtService.createAccessToken(member.getEmail(),member.getRole());
            String refreshToken=jwtService.createRefreshToken(member.getEmail(),member.getRole());
            jwtService.updateRefreshToken(member.getEmail(),refreshToken);
            return new Token(accessToken,refreshToken);
        } else {
            throw new CustomException(null, MEMBER_NOT_FOUND);
        }
    }

    /**
     * 권한 업데이트
     * @param member
     */
    @Transactional
    public void updateRole(Member member){
        member.authorizeUser();
        save(member);
    }

    /**
     * 닉네임 중복 검사
     */
    public boolean isDuplicateNickname(String nickname){
        Boolean exisitingNickname = false;

        try{
            exisitingNickname = memberRepository.existsByNickname(nickname);
        }catch (RuntimeException e){
            throw new CustomException(e, SERVER_ERROR);
        }

        return exisitingNickname;
    }

    /**
     * 회원 하나 조회
     */
    public Member findByMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(null, MEMBER_NOT_FOUND));
    }

    /**
     * 토큰으로 회원 조회
     */
    public Member findByMemberByToken(String token){
        String email = jwtService.getEmail(token);
        return findByMemberByEmail(email);
    }

    /**
     * 첫 로그인시 회원 업데이트 (회원 가입)
     */
    @Transactional
    public void joinMember(Member member, int age, boolean sex, String nickname){
        try{
            member.updateAge(age);
            member.updateSex(sex);
            member.updateNickname(nickname);
            updateRole(member);
        }catch (RuntimeException e){
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 닉네임 업데이트
     */
    @Transactional
    public void updateNickname(Member member, String nickname){
        isDuplicateNickname(nickname);
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

    /**
     * 성별 업데이트
     */
    @Transactional
    public void updateSex(Member member, boolean sex){
        member.updateSex(sex);
        save(member);
    }

    /**
     * 소셜 로그인
     */
    @Transactional
    public MemberLoginResponseDto loginMember(String accessToken, ProviderType provider){
        OAuth2UserDto profile = providerService.getProfile(accessToken,provider);
        Optional<Member> findMember = memberRepository.findByEmailAndProviderType(profile.getEmail(), provider);
        if (findMember.isPresent()){
            Member member = findMember.get();
            String xAuthToken = jwtService.createAccessToken(member.getEmail(), member.getRole());
            String rememberedToken = jwtService.createRefreshToken(member.getEmail(), member.getRole());
            jwtService.updateRefreshToken(member.getEmail(), rememberedToken);
            if (findMember.get().getRole() != Role.GUEST) {
                return new MemberLoginResponseDto(new Token(xAuthToken,rememberedToken),true);
            } else {
                return new MemberLoginResponseDto(new Token(xAuthToken,rememberedToken),false);
            }
        } else {
            Member member = Member.builder()
                    .email(profile.getEmail())
                    .nickname(profile.getName())
                    .providerType(provider)
                    .role(Role.GUEST)
                    .build();
            member = save(member);
            String xAuthToken=jwtService.createAccessToken(member.getEmail(),member.getRole());
            String rememberedToken=jwtService.createRefreshToken(member.getEmail(),member.getRole());
            jwtService.updateRefreshToken(member.getEmail(), rememberedToken);
            memberPhotoService.saveDefaultImage(member);
            return new MemberLoginResponseDto(new Token(xAuthToken,rememberedToken),false);
        }
    }

    @Transactional
    public void saveMemberPhoto(Member member, MultipartFile file) {
        if (member.getMemberPhoto() != null) {
            memberPhotoService.delete(member.getMemberPhoto());
        }
        memberPhotoService.saveMemberPhotos(member, file);
    }

    @Transactional
    public void updateFCMToken(Member member, String token) {
        member.updateFCMToken(token);
    }

    @Transactional
    public void deleteFCMToken(Member member) {
        memberRepository.updateFirebaseTokenToNull(member.getId());
    }

    public void checkAuthorization(Long memberId, Long checkMemberId) {
        if (!memberId.equals(checkMemberId)) {
            throw new CustomException(null, UNAUTHORIZED_ORDER);
        }
    }

    public Optional<Member> findByMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public boolean isTokenNullOrEmpty(String token){
        return token == null || token.isEmpty();
    }
}
