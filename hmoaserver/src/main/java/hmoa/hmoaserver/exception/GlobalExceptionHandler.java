package hmoa.hmoaserver.exception;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //커스텀 예외
    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ExceptionResponseDto> handleCustomException(CustomException e) {

        if (e.getCode().getHttpStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            Sentry.captureException(e);

        return ExceptionResponseDto.response(e.getCode());
    }

}
