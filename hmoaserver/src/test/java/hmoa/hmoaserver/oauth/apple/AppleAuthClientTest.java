package hmoa.hmoaserver.oauth.apple;

import hmoa.hmoaserver.oauth.service.AppleLoginService;
import hmoa.hmoaserver.oauth.userinfo.AppleOAuth2UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("default")
class AppleAuthClientTest {
    @Autowired
    AppleLoginService appleLoginService;

    @Test
    void getToken() {
        String authorizationCode = "eyJraWQiOiJmaDZCczhDIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLmh5dW5neXUuSE1PQS1pT1MiLCJleHAiOjE3MDEwNzQ2MDIsImlhdCI6MTcwMDk4ODIwMiwic3ViIjoiMDAwMTQwLjkyZGUyZmRlODljZjQxYWY5YWJiMjZiZTMzNDA4ZjgyLjAxNTEiLCJjX2hhc2giOiJYMlcxbEFzNzQ0c2xjYkFyWm4wS193IiwiYXV0aF90aW1lIjoxNzAwOTg4MjAyLCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.YJch3l89jUVV1jj0PsAvbvUUvnPUoDL9C7CLJhvLWDf3X8rAdNpVYDY5vuw1ZvGUV1hfDUXNs7SD0pnupsGRoEKMacpN8qzBLGl2k3IMnbAW0X9bCrpNSLPS5KVogeh1EAs5er_Rk275GTEEYhol7qUBYdEgk2RpRykH5k_0mHrZBTA1nzc4QLt9uHm3jN2lKw8NYfaVxxc4M6e5AlpJTB8jMBa5lXrX0fsheBE2xvCwRnyduBu_K__itVTPFSWsBtWAHnp4swZI2ajm_-vV7_bmJVxpuTzfHXmNV24LOWTau6Hp8eBq1jlivDxYEiCjGBH6WcEa8bUiBWFKGBAfIg";

        AppleOAuth2UserInfo userInfo = appleLoginService.get(authorizationCode);
        System.out.println(userInfo.getSub());
        System.out.println(userInfo.getName());
    }
}