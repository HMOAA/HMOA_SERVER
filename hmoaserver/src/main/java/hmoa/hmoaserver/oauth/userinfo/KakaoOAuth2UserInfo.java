package hmoa.hmoaserver.oauth.userinfo;

import lombok.*;

@Getter
public class KakaoOAuth2UserInfo {
    private KakaoAccount kakao_account;
    private Properties properties;
    @Getter
    @ToString
    public class KakaoAccount{
        private String email;

    }

    @Getter
    @ToString
    public class Properties{
        private String nickname;
    }

}
