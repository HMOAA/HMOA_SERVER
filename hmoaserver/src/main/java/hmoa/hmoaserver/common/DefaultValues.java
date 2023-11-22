package hmoa.hmoaserver.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultValues {
    private static String profileUrl;

    @Value("${default.profile}")
    public void setProfileUrl(String profile) {
        profileUrl = profile;
    }

    public static String getProfileUrl() {
        log.info(profileUrl);
        return profileUrl;
    }
}
