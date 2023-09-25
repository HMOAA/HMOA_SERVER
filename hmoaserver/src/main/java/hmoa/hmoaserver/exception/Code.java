package hmoa.hmoaserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum Code {
    /**
     *  400 BAD_REQUEST
     */
    BOARD_NOT_FOUND(BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    ACCESS_DENIED(BAD_REQUEST, "권한이 없습니다."),

    /**
     * 401 UNAUTHORIZED
     */
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "변조된 토큰입니다."),
    WRONG_TYPE_TOKEN(UNAUTHORIZED, "변조된 토큰입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "로그인이 필요합니다."),
    UNAUTHORIZED_COMMENT(UNAUTHORIZED, "자신이 쓴 댓글만 수정이 가능합니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN_AUTHORIZATION(FORBIDDEN,"접근 권한이 없습니다"),

    /**
     * 404 NOT_FOUND
     */
    UNKNOWN_ERROR(NOT_FOUND, "토큰이 존재하지 않습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "등록된 파일이 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "등록된 멤버가 없습니다."),
    BRAND_NOT_FOUND(NOT_FOUND, "등록된 브랜드가 없습니다."),
    PERFUME_NOT_FOUND(NOT_FOUND, "등록된 향수가 없습니다."),
    TERM_NOT_FOUND(NOT_FOUND, "등록된 용어가 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "등록된 댓글이 없습니다."),
    HEART_NOT_FOUND(NOT_FOUND, "등록된 하트가 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "등록된 리뷰가 없습니다."),
    BRANDLIKEDMEMBER_NOT_FOUND(NOT_FOUND, "멤버가 브랜드에 공감하지 않았습니다."),
    PERFUMELIKEDMEMBER_NOT_FOUND(NOT_FOUND, "멤버가 향수에 공감하지 않았습니다."),

    /**
     * 409 CONFLICT
     */
    DUPLICATE_NICKNAME(CONFLICT,"이미 존재하는 닉네임 입니다."),
    DUPLICATE_LIKED(CONFLICT, "공감은 한 번만 가능합니다."),
    DUPLICATE_WEATHERIDX(CONFLICT, "올바른 숫자로 보내주세요"),
    DUPLICATE_GENDERIDX(CONFLICT, "올바른 숫자로 보내주세요"),
    DUPLICATE_AGEIDX(CONFLICT, "올바른 숫자로 보내주세요"),

    /**
     * 415 UNSUPPORTED_MEDIA_TYPE
     */
    FILE_TYPE_UNSUPPORTED(UNSUPPORTED_MEDIA_TYPE, "파일 형식은 '.jpg', '.jpeg', '.png' 만 가능합니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR,"서버 에러 발생");

    private final HttpStatus httpStatus;
    private final String message;


}
