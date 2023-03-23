package hmoa.hmoaserver.oauth;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.ProviderType;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.oauth.userinfo.GoogleOAuth2UserInfo;
import hmoa.hmoaserver.oauth.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo){
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo=oauth2UserInfo;
    }

    public static OAuthAttributes of(ProviderType providerType, String userNameAttributeName, Map<String, Object> attributes){
        return ofGoogle(userNameAttributeName,attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(ProviderType providerType, OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .providerType(providerType)
                .socialId(oauth2UserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(oauth2UserInfo.getNickname())
                .imgUrl(oauth2UserInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }
}
