package hmoa.hmoaserver.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultValues {
    private static String profileUrl;
    private static String mainImgUrl;

    @Value("${default.profile}")
    public void setProfileUrl(String profile) {
        profileUrl = profile;
    }

    @Value("${default.main}")
    public void setMainImgUrl(String mainImg) {
        mainImgUrl = mainImg;
    }

    public static String getProfileUrl() {
        log.info(profileUrl);
        return profileUrl;
    }

    public static String getMainImgUrl() {
        return mainImgUrl;
    }
}
