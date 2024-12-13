package hmoa.hmoaserver.oauth.jwt.filter;


import com.google.gson.JsonObject;

import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static hmoa.hmoaserver.exception.Code.*;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final List<String> NO_CHECK_URL = List.of(
            "/login/**",
            "/login"
    );


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return NO_CHECK_URL.stream().anyMatch(url -> {
            if (url.endsWith("/**")){
                return path.startsWith(url.substring(0, url.length() -3));
            } else {
                return url.equalsIgnoreCase(path);
            }
        });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("필터");

        // jwt 에러 시 401 또는 404를, 이외의 예외는 서버 에러로 간주한다 (서버 에러 사항은 로그를 확인해볼것)
        try {
            if (jwtService.extractAccessToken(request).isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                errorResult(response, HttpServletResponse.SC_NOT_FOUND, UNKNOWN_ERROR.getMessage());
            }

            String token = jwtService.extractAccessToken(request).get();
            JwtResultType jwtResultType = jwtService.isTokenValid(token);

            if (jwtResultType == JwtResultType.VALID_JWT) {
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else if (jwtResultType == JwtResultType.EXPIRED_JWT) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorResult(response, HttpServletResponse.SC_UNAUTHORIZED, EXPIRED_TOKEN.getMessage());
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorResult(response, HttpServletResponse.SC_UNAUTHORIZED, WRONG_TYPE_TOKEN.getMessage());
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.info("SERVER_ERROR 발생 확인 바람.");
            errorResult(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void errorResult(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        JsonObject json = new JsonObject();
        json.addProperty("code", code);
        json.addProperty("message", message);
        response.getWriter().print(json);
    }
}
