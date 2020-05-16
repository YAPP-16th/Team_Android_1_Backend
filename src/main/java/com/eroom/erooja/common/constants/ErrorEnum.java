package com.eroom.erooja.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    /* JWT 오류 메세지 정의 */
    JWT_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JWT_000", "알 수 없는 JWT 오류입니다."),
    JWT_MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "JWT_001", "잘못된 JWT 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.BAD_REQUEST, "JWT_002", "미지원 JWT 토큰 방식입니다."),
    JWT_EXPIRED(HttpStatus.BAD_REQUEST, "JWT_003", "만료된 토큰입니다."),
    /* JWT 오류 메세지 정의 끝 */

    /* 인증 오류 메세지 정의 */
    AUTH_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "ATH_001", "접근 권한이 없습니다."),
    /* 인증 오류 메세지 정의 끝 */

    /* 카카오톡 인증 오류 메세지 정의 */
    AUTH_KAKAO_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "KKO_000", "알 수 없는 카카오 통신 모듈 오류입니다."),
    AUTH_KAKAO_NOT_SUPPORTED_METHOD(HttpStatus.BAD_REQUEST, "KKO_001", "지원하지 않는 인증 방식 입니다."),
    AUTH_KAKAO_NOT_REGISTERED_USER(HttpStatus.NOT_FOUND, "KKO_002", "등록되지 않은 카카오 유저입니다."),
    /* 카카오톡 인증 오류 메세지 정의 끝 */

    /* 멤버 CRUD 오류 메세지 정의 */
    MEMBER_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MBS_000", "알 수 없는 사용자 관리 서비스 오류입니다."),
    MEMBER_DUPLICATED_PROPS(HttpStatus.CONFLICT, "MBS_001", "중복된 속성입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MBS_002", "해당 UID 의 유저를 찾을 수 없습니다."),

    MEMBER_JOB_INTEREST_INVALID_BODY(HttpStatus.BAD_REQUEST, "MJI_001", "올바르지 않은 리퀘스트 바디입니다."),
    MEMBER_JOB_INTEREST_ALREADY_EXISTS(HttpStatus.CONFLICT, "MJI_002", "이미 존재하는 직군 혹은 직무입니다."),

    MEMBER_IMAGE_DELETE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MID_001", "삭제할 수 없는 이미지입니다."),
    /* 멤버 CRUD 오류 메세지 정의 끝*/

    /* 직군 CRUD 오류 메세지 정의 */
    JOB_INTEREST_NOT_EXISTS(HttpStatus.NOT_FOUND, "JIT_001", "존재하지 않는 직군 혹은 직무입니다."),
    /* 직군 CRUD 오류 메세지 정의 끝 */

    /* 목표 관련 오류 메세지 정의 */
    GOAL_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GOL_000", "알 수 없는 목표(Goal) 관련 오류입니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "GOL_001", "해당 목표가 존재하지 않습니다."),
    GOAL_INVALID_ARGS(HttpStatus.BAD_REQUEST, "GOL_002", "올바르지 않은 파라미터, 혹은 요청 바디입니다."),
    GOAL_AUTH_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "GOL_003", "목표에 대한 권한이없습니다."),
    GOAL_TERMINATED(HttpStatus.CONFLICT, "GOL_004", "종료된 목표입니다."),
    /* 목표 관련 오류 메세지 정의 끝 */

    /* 할일 CRUD 오류 메세지 정의 */
    TODO_PRIORITY_NOT_CORRECT(HttpStatus.BAD_REQUEST, "TOD_001", "할일 우선순위는 0부터 1씩 차례대로 존재해야합니다."),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TOD_001", "존재하지 않은 할일입니다."),
    TODO_NOT_OWNER(HttpStatus.CONFLICT, "TOD_001", "자신의 것이 아닌 할일입니다."),
    /* 할일 CRUD 오류 메세지 정의 끝 */

    /* 목표참여 CRUD 오류 메세지 정의 */
    GOAL_JOIN_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "GOJ_001", "이미 목표담기를 했습니다. 중복담기 불가능합니다."),
    GOAL_JOIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "GOJ_002", "참여하고 있지 않는 목표입니다."),
    /* 목표참여 CRUD 오류 메세지 정의 끝 */

    /* 알람 CRUD 오류 메세지 정의 */
    ALARM_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "ALM_001", "존재하지 않는 알람 메시지입니다."),
    ALARM_MESSAGE_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "ALM_001", "알림메시지에 대한 권한이 없습니다."),
    /* 목표참여 CRUD 오류 메세지 정의 끝 */

    ETC(HttpStatus.INTERNAL_SERVER_ERROR, "ETC_000", "알 수 없는 오류입니다.");

    private ErrorResponse errorResponse;

    public String getMessage() {
        return this.errorResponse.getMessage();
    }

    public String getErrCode() {
        return this.errorResponse.getErrCode();
    }

    public HttpStatus getHttpStatus() {
        return this.errorResponse.getHttpStatus();
    }

    ErrorEnum(HttpStatus httpStatus, String errCode, String message) {
        this.errorResponse = new ErrorResponse(httpStatus, errCode, message);
    }

    @Getter
    public class ErrorResponse {
        private HttpStatus httpStatus;
        private String errCode;
        private String message;

        public ErrorResponse(HttpStatus httpStatus, String errCode, String message) {
            this.httpStatus = httpStatus;
            this.errCode = errCode;
            this.message = message;
        }
    }
}
