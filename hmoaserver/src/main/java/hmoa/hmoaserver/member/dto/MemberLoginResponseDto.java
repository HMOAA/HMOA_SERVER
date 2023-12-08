package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.oauth.jwt.Token;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginResponseDto {
    private String authToken;
    private String rememberedToken;
    private Boolean existedMember;

    public MemberLoginResponseDto(Token token, boolean exist){
        this.authToken = token.getAuthToken();
        this.rememberedToken = token.getRememberedToken();
        this.existedMember = exist;
    }
}
