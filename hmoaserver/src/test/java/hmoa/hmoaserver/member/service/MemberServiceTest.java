package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.oauth.jwt.Token;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.oauth.service.ProviderService;
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
        String refreshToken = jwtService.createRefreshToken("TEST", Role.USER);
        Member member = createMember();
        when(jwtService.isTokenValid(refreshToken)).thenReturn(JwtResultType.VALID_JWT);
        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(member));
        when(jwtService.createAccessToken(member.getEmail(), member.getRole())).thenReturn("NEW_ACCESS_TOKEN");
        when(jwtService.createRefreshToken(member.getEmail(), member.getRole())).thenReturn("NEW_REFRESH_TOKEN");

        //when
        Token token = memberService.reissueTokens(refreshToken);

        //then
        assertNotNull(token);
        assertEquals("NEW_ACCESS_TOKEN", token.getAuthToken());
        assertEquals("NEW_REFRESH_TOKEN", token.getRememberedToken());
        verify(jwtService).updateRefreshToken(member.getEmail(), "NEW_REFRESH_TOKEN");
    }

    private Member createMember() {
        return Member.builder()
                .email("TEST")
                .role(Role.USER)
                .build();
    }
}