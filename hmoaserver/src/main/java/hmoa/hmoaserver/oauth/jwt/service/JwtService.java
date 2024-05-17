package hmoa.hmoaserver.oauth.jwt.service;


import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Role;
import io.jsonwebtoken.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.oauth.jwt.Token;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * subject와 claim으로 email
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";

    private final MemberRepository memberRepository;

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    //accessToken 생성
    public String createAccessToken(String email, Role roles){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles",roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    //refreshToken 생성
    public String createRefreshToken(String email, Role roles) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        log.info("{}", refreshToken);
        return refreshToken;
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    //access+refresh json 형태로 보내기
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        log.info("sendAccessAndRefreshToken");
        Token token = new Token(accessToken,refreshToken);
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(token);
        log.info("{}",result);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
    }

    /**
     * 헤더에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader));
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(user -> {
                    user.updateRefreshToken(refreshToken);
                    memberRepository.saveAndFlush(user);},
                    () -> new CustomException(null, MEMBER_NOT_FOUND)
                );
    }

    public JwtResultType isTokenValid(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return JwtResultType.VALID_JWT;
        }catch (ExpiredJwtException e) {
            log.info("만료된 jwt 토큰");
            return JwtResultType.EXPIRED_JWT;
        }catch (Exception e) {
            log.info("잘못된 jwt 토큰");
            return JwtResultType.INVALID_JWT;
        }
    }

}
