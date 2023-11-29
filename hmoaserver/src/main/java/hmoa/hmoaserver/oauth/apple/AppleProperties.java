package hmoa.hmoaserver.oauth.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.social.apple")
@Getter
@Setter
public class AppleProperties {
    private String grantType;
    private String clientId;
    private String keyId;
    private String teamId;
    private String audience;
    private String privateKey;
}
