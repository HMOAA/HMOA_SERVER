package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.ProviderType;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.member.dto.MemberLoginResponseDto;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.oauth.jwt.Token;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.oauth.service.ProviderService;
import hmoa.hmoaserver.oauth.userinfo.OAuth2UserDto;
import hmoa.hmoaserver.photo.service.MemberPhotoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ProviderService providerService;

    @Mock
    private MemberPhotoService memberPhotoService;

    @Test
    @DisplayName("올바른_리프레쉬_토큰이_들어오면_재생성하고_멤버에_업데이트한다.")
    void reissue_validToken() {
        //given
        String refreshToken = jwtService.createRefreshToken();
        Member member = createMember();
        when(jwtService.isTokenValid(refreshToken)).thenReturn(JwtResultType.VALID_JWT);
        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(member));
        when(jwtService.createAccessAndRefreshToken(member.getEmail(), member.getRole())).thenReturn(new Token("NEW_ACCESS_TOKEN", "NEW_REFRESH_TOKEN"));

        //when
        Token token = memberService.reissueTokens(refreshToken);

        //then
        assertNotNull(token);
        assertEquals("NEW_ACCESS_TOKEN", token.getAuthToken());
        assertEquals("NEW_REFRESH_TOKEN", token.getRememberedToken());
        verify(jwtService).updateRefreshToken(member.getEmail(), "NEW_REFRESH_TOKEN");
    }

    @Test
    @DisplayName("잘못된_토큰이_들어오면_401_에러를_반환한다.")
    void reissue_invalidRefreshToken() {
        //given
        String invalidRefreshToken = "INVALID_REFRESH_TOKEN";
        when(jwtService.isTokenValid(invalidRefreshToken)).thenReturn(JwtResultType.INVALID_JWT);

        // when, then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.reissueTokens(invalidRefreshToken));
        assertEquals("변조된 토큰입니다.", exception.getCode().getMessage());

        //verify
        verify(jwtService).isTokenValid(invalidRefreshToken);
        verifyNoInteractions(memberRepository);
    }

    @Test
    @DisplayName("중복_되지_않은_닉네임이_들어오면_false를_반환한다.")
    void nonExistingNickname_returnFalse() {
        // given
        String nickname = "unique_nickname";
        when(memberRepository.existsByNickname(nickname)).thenReturn(false);

        // when
        boolean result = memberService.isDuplicateNickname(nickname);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("중복된_닉네임이_들어오면_true를_반환한다.")
    void duplicateNickname_returnTrue() {
        //given
        String nickname = "duplicate_nickname";
        when(memberRepository.existsByNickname(nickname)).thenReturn(true);

        // when
        boolean result = memberService.isDuplicateNickname(nickname);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("회원이 이미 존재하고 GUEST 권한이 아닌 경우 로그인에 성공한다.")
    void loginMember_existingAndNonGuest() {
        // given
        String accessToken = "accessToken";
        ProviderType provider = ProviderType.GOOGLE;
        OAuth2UserDto profile = new OAuth2UserDto("member@test.com", "test");
        Member existingMember = createMember("member@test.com", "test", Role.USER);
        Token token = new Token("xAuthToken", "refreshToken");

        when(providerService.getProfile(accessToken, provider)).thenReturn(profile);
        when(jwtService.createAccessAndRefreshToken(existingMember.getEmail(), existingMember.getRole())).thenReturn(token);
        when(memberRepository.findByEmailAndProviderType(existingMember.getEmail(), provider)).thenReturn(Optional.of(existingMember));

        // when
        MemberLoginResponseDto result = memberService.loginMember(accessToken, provider);

        // then
        assertNotNull(result);
        assertTrue(result.getExistedMember());
        assertEquals(token.getAuthToken(), result.getAuthToken());
        assertEquals(token.getRememberedToken(), result.getRememberedToken());
        verify(jwtService).updateRefreshToken(existingMember.getEmail(), "refreshToken");
        verify(memberPhotoService, never()).saveDefaultImage(any());
    }

    @Test
    @DisplayName("회원이 이미 존재하지만 GUEST 권한인 경우 회원가입이 필요하다는 것을 반환한다.")
    void loginMember_existingAndGuest() {
        //given
        String accessToken = "accessToken";
        ProviderType provider = ProviderType.GOOGLE;
        OAuth2UserDto profile = new OAuth2UserDto("member@test.com", "test");
        Member existingMember = createMember("member@test.com", "test", Role.GUEST);
        Token token = new Token("xAuthToken", "refreshToken");

        when(providerService.getProfile(accessToken, provider)).thenReturn(profile);
        when(memberRepository.findByEmailAndProviderType(existingMember.getEmail(), provider)).thenReturn(Optional.of(existingMember));
        when(jwtService.createAccessAndRefreshToken(existingMember.getEmail(), existingMember.getRole())).thenReturn(token);

        // when
        MemberLoginResponseDto result = memberService.loginMember(accessToken, provider);

        // then
        assertNotNull(result);
        assertFalse(result.getExistedMember());
        assertEquals(token.getAuthToken(), result.getAuthToken());
        assertEquals(token.getRememberedToken(), result.getRememberedToken());
        verify(jwtService).updateRefreshToken(existingMember.getEmail(), "refreshToken");
        verify(memberPhotoService, never()).saveDefaultImage(any());
    }

    @Test
    @DisplayName("회원이 존재하지 않을 경우 새 회원을 생성하고 토큰을 반환한다.")
    void loginMember_newMember() {
        // given
        String accessToken = "accessToken";
        ProviderType provider = ProviderType.GOOGLE;
        Token token = new Token("xAuthToken", "refreshToken");
        OAuth2UserDto profile = new OAuth2UserDto("member@test.com", "test");
        Member newMember = createMember(profile.getEmail(), "test", Role.GUEST);

        when(providerService.getProfile(accessToken, provider)).thenReturn(profile);
        when(jwtService.createAccessAndRefreshToken(newMember.getEmail(), newMember.getRole())).thenReturn(token);
        when(memberRepository.save(newMember)).thenReturn(newMember);

        // when
        MemberLoginResponseDto result = memberService.loginMember(accessToken, provider);

        //then
        assertNotNull(result);
        assertFalse(result.getExistedMember());
        assertEquals(token.getAuthToken(), result.getAuthToken());
        assertEquals(token.getRememberedToken(), result.getRememberedToken());
        verify(memberRepository).save(newMember);
        verify(memberPhotoService).saveDefaultImage(newMember);
    }

    private Member createMember() {
        return Member.builder()
                .email("TEST")
                .role(Role.USER)
                .build();
    }

    private Member createMember(String email, String nickname, Role role) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();
    }
}