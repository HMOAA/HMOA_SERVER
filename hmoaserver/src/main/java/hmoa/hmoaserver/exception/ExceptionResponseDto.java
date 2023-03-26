package hmoa.hmoaserver.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ExceptionResponseDto {
    private String code;
    private String message;

    public static ResponseEntity<ExceptionResponseDto> response(final Code code){
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ExceptionResponseDto.builder()
                        .code(code.name())
                        .message(code.getMessage())
                        .build());
    }
}
