package hmoa.hmoaserver.oauth.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class Token {
    private String authToken;
    private String rememberedToken;

    public Token(String token, String refreshToken) {
        this.authToken = token;
        this.rememberedToken = refreshToken;
    }
}
