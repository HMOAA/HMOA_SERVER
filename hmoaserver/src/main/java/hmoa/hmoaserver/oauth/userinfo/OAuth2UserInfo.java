package hmoa.hmoaserver.oauth.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    //소셜 식별 값
    public abstract String getId();

    public abstract String getNickname();

    public abstract String getImageUrl();
}