package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.oauth.jwt.Token;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {
    private String authToken;
    private String rememberedToken;

    public TokenResponseDto(Token token){
        this.authToken=token.getAuthToken();
        this.rememberedToken=token.getRememberedToken();
    }
}
