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

    MEMBER_JOB_INTEREST_INVALID_BODY(HttpStatus.BAD_REQUEST, "MJI_001", "올바르지 않은 리퀘스트 바디입니다."),
    MEMBER_JOB_INTEREST_ALREADY_EXISTS(HttpStatus.CONFLICT, "MJI_002", "이미 존재하는 직군 혹은 직무입니다."),
    /* 멤버 CRUD 오류 메세지 정의 끝*/

    /* 직군 CRUD 오류 메세지 정의 */
    JOB_INTEREST_NOT_EXISTS(HttpStatus.NOT_FOUND, "JIT_001", "존재하지 않는 직군 혹은 직무입니다."),
    /* 직군 CRUD 오류 메세지 정의 끝 */


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

        public ErrorResponse(String errCode, String message) {
            this.errCode = errCode;
            this.message = message;
        }
    }
}
