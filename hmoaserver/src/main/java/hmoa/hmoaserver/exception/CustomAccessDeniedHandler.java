package hmoa.hmoaserver.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static ExceptionResponseDto errorResponse=
            new ExceptionResponseDto(
                    Code.FORBIDDEN_AUTHORIZATION.name(),
                    Code.FORBIDDEN_AUTHORIZATION.getMessage()
            );
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        JsonObject json = new JsonObject();
        json.addProperty("code", errorResponse.getCode());
        json.addProperty("message", errorResponse.getMessage());
        response.getWriter().print(json);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
