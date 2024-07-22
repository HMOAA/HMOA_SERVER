package hmoa.hmoaserver.exception;

import com.google.gson.JsonObject;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtService jwtService;

    private static final int ERROR_CODE = 401;
    private static final int EMPTY_CODE = 404;
    private static final String CODE_MESSAGE = "code";
    private static final String MESSAGE_MESSAGE = "message";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        JwtResultType errorType = isTokenValid(request.getHeader("X-AUTH-TOKEN"));

        response.setContentType("application/json;charset=utf-8");

        JsonObject json = getJsonObject(errorType);
        response.setStatus(json.get(CODE_MESSAGE).getAsInt());

        log.info("{}", json.toString());

        response.getWriter().print(json);
    }

    private JwtResultType isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return JwtResultType.EMPTY_JWT;
        }

        return jwtService.isTokenValid(token);
    }

    private static JsonObject getJsonObject(JwtResultType type) {
        JsonObject json = new JsonObject();
        if (type.equals(JwtResultType.EMPTY_JWT)) {
            json.addProperty(CODE_MESSAGE, EMPTY_CODE);
            json.addProperty(MESSAGE_MESSAGE, Code.UNKNOWN_ERROR.getMessage());
            return json;
        }

        json.addProperty(CODE_MESSAGE, ERROR_CODE);
        if (type.equals(JwtResultType.EXPIRED_JWT)) {
            json.addProperty(MESSAGE_MESSAGE, Code.EXPIRED_TOKEN.getMessage());
        } else if (type.equals(JwtResultType.INVALID_JWT)) {
            json.addProperty(MESSAGE_MESSAGE, Code.WRONG_TYPE_TOKEN.getMessage());
        } else {
            json.addProperty(MESSAGE_MESSAGE, Code.UNSUPPORTED_TOKEN.getMessage());
        }

        return json;
    }
}
