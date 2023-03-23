//package hmoa.hmoaserver.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import net.minidev.json.JSONObject;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        String exception = (String)request.getAttribute("exception");
//
//        if(exception == null) {
//            log.info("알수 없는 에러");
//            setResponse(response, Code.UNKNOWN_ERROR);
//        }
//        //잘못된 타입의 토큰인 경우
//        else if(exception.equals(Code.WRONG_TYPE_TOKEN)) {
//            log.info("wrongtype error");
//            setResponse(response, Code.WRONG_TYPE_TOKEN);
//        }
//        //토큰 만료된 경우
//        else if(exception.equals(Code.EXPIRED_TOKEN)) {
//            log.info("토큰 만료");
//            setResponse(response, Code.EXPIRED_TOKEN);
//        }
//        //지원되지 않는 토큰인 경우
//        else if(exception.equals(Code.UNSUPPORTED_TOKEN)) {
//            log.info("지원되지 않는 토큰");
//            setResponse(response, Code.UNSUPPORTED_TOKEN);
//        }
//        else {
//            log.info("access_denied");
//            setResponse(response, Code.ACCESS_DENIED);
//        }
//    }
//    //한글 출력을 위해 getWriter() 사용
//    private void setResponse(HttpServletResponse response, Code code) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("message", code.getMessage());
//        responseJson.put("code", code.getHttpStatus());
//
//        response.getWriter().print(responseJson);
//    }
//}
