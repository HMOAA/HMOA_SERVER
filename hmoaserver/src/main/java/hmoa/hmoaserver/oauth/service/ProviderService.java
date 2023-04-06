package hmoa.hmoaserver.oauth.service;


import com.google.gson.Gson;
import hmoa.hmoaserver.member.domain.ProviderType;
import hmoa.hmoaserver.oauth.userinfo.GoogleOAuth2UserInfo;
import hmoa.hmoaserver.oauth.userinfo.KakaoOAuth2UserInfo;
import hmoa.hmoaserver.oauth.userinfo.OAuth2UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.naming.CommunicationException;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderService {
    private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/login/oauth2/";
    private final RestTemplate restTemplate;
    @Value("${spring.social.google.url.profile}")
    private String googleUrl;

    @Value("${spring.social.kakao.url.profile}")
    private String kakaoUrl;

    private final Gson gson;

    public OAuth2UserDto getProfile(String accessToken, ProviderType provider) {
        log.info("getProfile");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.set("Authorization", "Bearer " + accessToken);


        String profileUrl = urlMapping(provider);
        log.info("{}",profileUrl);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(profileUrl, request, String.class);
        log.info("{}",response.getHeaders());


        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("{}",response);
                return extractProfile(response, provider);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    private String urlMapping(ProviderType checkProvider){
        if(checkProvider.equals(ProviderType.GOOGLE)){
            return googleUrl;
        }else if(checkProvider.equals(ProviderType.KAKAO)){
            return kakaoUrl;
        }
        return googleUrl;
    }
    private OAuth2UserDto extractProfile(ResponseEntity<String> response, ProviderType provider) {
        if (provider.equals(ProviderType.GOOGLE)) {
            GoogleOAuth2UserInfo googleOAuth2UserInfo = gson.fromJson(response.getBody(), GoogleOAuth2UserInfo.class);
            return new OAuth2UserDto(googleOAuth2UserInfo.getEmail(),googleOAuth2UserInfo.getName());
        } else if (provider.equals(ProviderType.KAKAO)) {
            KakaoOAuth2UserInfo kakaoOAuth2UserInfo = gson.fromJson(response.getBody(), KakaoOAuth2UserInfo.class);
            return new OAuth2UserDto(kakaoOAuth2UserInfo.getKakao_account().getEmail(),kakaoOAuth2UserInfo.getProperties().getNickname());

        }
        GoogleOAuth2UserInfo googleOAuth2UserInfo = gson.fromJson(response.getBody(), GoogleOAuth2UserInfo.class);
        return new OAuth2UserDto(googleOAuth2UserInfo.getEmail(),googleOAuth2UserInfo.getName());
    }
}
