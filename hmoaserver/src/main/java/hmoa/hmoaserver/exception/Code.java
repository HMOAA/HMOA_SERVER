package hmoa.hmoaserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum Code {

    INDEX_NOT_FOUND(NOT_FOUND, "인덱스가 존재하지 않습니다."),
    BOARD_NOT_FOUND(BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    UNKNOWN_ERROR(NOT_FOUND, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(BAD_REQUEST, "변조된 토큰입니다."),
    EXPIRED_TOKEN(BAD_REQUEST, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(BAD_REQUEST, "변조된 토큰입니다."),
    ACCESS_DENIED(BAD_REQUEST, "권한이 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "등록된 멤버가 없습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR,"서버 에러 발생");

    private final HttpStatus httpStatus;
    private final String message;


}
