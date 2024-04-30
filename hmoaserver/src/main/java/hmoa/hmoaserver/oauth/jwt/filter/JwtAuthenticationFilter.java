package hmoa.hmoaserver.oauth.jwt.filter;


import com.google.gson.JsonObject;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.oauth.jwt.service.JwtResultType;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.oauth.jwt.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hmoa.hmoaserver.exception.Code.*;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static int ERROR_CODE = 401;
    private final static int BAD_ERROR_CODE = 404;

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
        if (jwtService.extractAccessToken(request).isPresent() ) {
            String token = jwtService.extractAccessToken(request).get();
            JwtResultType jwtResultType = jwtService.isTokenValid(token);
            if (jwtResultType == JwtResultType.VALID_JWT) {
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else if(jwtResultType == JwtResultType.EXPIRED_JWT){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorResult(response, ERROR_CODE, EXPIRED_TOKEN.getMessage());
            } else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorResult(response, ERROR_CODE, WRONG_TYPE_TOKEN.getMessage());
            }
        }else{
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            errorResult(response, BAD_ERROR_CODE, UNKNOWN_ERROR.getMessage());
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
