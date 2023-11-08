package hmoa.hmoaserver.common;

import org.springframework.beans.factory.annotation.Value;

public class DefaultValues {
    @Value("${defalut.profile}")
    private static String DEFALUT_PROFILE_URL;

    public static String getDEFALUT_PROFILE_URL() {
        return DEFALUT_PROFILE_URL;
    }
}
