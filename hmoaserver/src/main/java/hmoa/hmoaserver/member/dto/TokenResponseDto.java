package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.oauth.jwt.Token;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(Token token){
        this.accessToken=token.getToken();
        this.refreshToken=token.getRefreshToken();
    }
}
