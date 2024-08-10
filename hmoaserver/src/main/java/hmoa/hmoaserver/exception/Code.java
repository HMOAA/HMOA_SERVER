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
    APPLE_DECODE_ERROR(BAD_REQUEST, "애플 소셜 로그인 호출 오류"),

    /**
     * 401 UNAUTHORIZED
     */
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "변조된 토큰입니다."),
    WRONG_TYPE_TOKEN(UNAUTHORIZED, "변조된 토큰입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "ACCESS Token이 만료되었습니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "로그인이 필요합니다."),
    UNAUTHORIZED_COMMENT(UNAUTHORIZED, "자신이 쓴 댓글만 수정이 가능합니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN_AUTHORIZATION(FORBIDDEN,"접근 권한이 없습니다"),

    /**
     * 404 NOT_FOUND
     */
    UNKNOWN_ERROR(NOT_FOUND, "jwt가 존재하지 않습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "등록된 파일이 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "등록된 멤버가 없습니다."),
    BRAND_NOT_FOUND(NOT_FOUND, "등록된 브랜드가 없습니다."),
    PERFUME_NOT_FOUND(NOT_FOUND, "등록된 향수가 없습니다."),
    TERM_NOT_FOUND(NOT_FOUND, "등록된 용어가 없습니다."),
    NOTE_NOT_FOUND(NOT_FOUND, "등록된 노트가 없습니다."),
    PERFUMER_NOT_FOUND(NOT_FOUND, "등록된 조향사가 없습니다."),
    BRANDSTORY_NOT_FOUND(NOT_FOUND, "등록된 브랜드스토리가 없습니다."),
    COMMUNITYPHOTO_NOT_FOUND(NOT_FOUND, "등록된 게시글 사진이 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "등록된 댓글이 없습니다."),
    HEART_NOT_FOUND(NOT_FOUND, "등록된 하트가 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "등록된 리뷰가 없습니다."),
    BRANDLIKEDMEMBER_NOT_FOUND(NOT_FOUND, "멤버가 브랜드에 공감하지 않았습니다."),
    PERFUMELIKEDMEMBER_NOT_FOUND(NOT_FOUND, "멤버가 향수에 공감하지 않았습니다."),
    COMMUNITY_NOT_FOUND(NOT_FOUND, "등록된 게시글이 없습니다"),
    HOMEMENU_NOT_FOUND(NOT_FOUND, "등록된 홈 메뉴가 없습니다."),
    COMMUNITYLIKEDMEMEBER_NOT_FOUND(NOT_FOUND, "등록된 커뮤니티 좋아요가 없습니다."),
    COMMUNITYCOMMENTLIKEDMEMBER_NOT_FOUND(NOT_FOUND, "등록된 커뮤니티 댓글 좋아요가 없습니다."),
    MAGAZINE_NOT_FOUND(NOT_FOUND, "등록된 매거진이 없습니다"),
    MAGAZINELIKEDMEMBER_NOT_FOUND(NOT_FOUND, "등록된 매거진 좋아요가 없습니다."),
    PUSH_ALARM_NOT_FOUND(NOT_FOUND, "등록된 푸쉬 알림이 없습니다."),
    SURVEY_NOT_FOUND(NOT_FOUND, "등록된 설문이 없습니다."),
    QUESTION_NOT_FOUND(NOT_FOUND, "등록된 질문이 없습니다."),
    ANSWER_NOT_FOUND(NOT_FOUND, "등록된 답변이 없습니다."),
    CART_NOT_FOUND(NOT_FOUND, "저장된 장바구니가 없습니다."),
    MEMBER_INFO_NOT_FOUND(NOT_FOUND, "저장된 주문자 정보가 없습니다"),

    /**
     * 409 CONFLICT
     */
    DUPLICATE_NICKNAME(CONFLICT,"이미 존재하는 닉네임 입니다."),
    DUPLICATE_LIKED(CONFLICT, "공감은 한 번만 가능합니다."),
    DUPLICATE_WEATHERIDX(CONFLICT, "올바른 숫자로 보내주세요"),
    DUPLICATE_GENDERIDX(CONFLICT, "올바른 숫자로 보내주세요"),
    DUPLICATE_AGEIDX(CONFLICT, "올바른 숫자로 보내주세요"),

    /**
     * 413 PAYLOAD_TOO_LARGE
     */
    FILE_SIZE_EXCEED(PAYLOAD_TOO_LARGE, "파일 용량이 초과되었습니다"),
    FILE_COUNT_EXCEED(PAYLOAD_TOO_LARGE, "파일 개수가 초과되었습니다"),

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
