package hmoa.hmoaserver.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final int ERROR_CODE = 401;

    @Value("${jwt.secret}")
    private String secretKey;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        JwtResultType errorType = isTokenValid(request.getHeader("X-AUTH-TOKEN"));
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(401);
        JsonObject json = new JsonObject();
        json.addProperty("code", ERROR_CODE);

        if (errorType.equals(JwtResultType.EXPIRED_JWT)) {
            json.addProperty("message", Code.EXPIRED_TOKEN.getMessage());
        } else if (errorType.equals(JwtResultType.INVALID_JWT)) {
            json.addProperty("message", Code.WRONG_TYPE_TOKEN.getMessage());
        } else {
            json.addProperty("message", Code.UNSUPPORTED_TOKEN.getMessage());
        }
        response.getWriter().print(json);
    }

    private JwtResultType isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(secretKey);
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
