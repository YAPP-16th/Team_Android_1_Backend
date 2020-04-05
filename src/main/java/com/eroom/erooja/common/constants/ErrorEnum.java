package com.eroom.erooja.common.constants;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    /* JWT 오류 메세지 정의 */
    JWT_UNKNOWN_ERROR("JWT_000", "알 수 없는 JWT 오류입니다."),
    JWT_MALFORMED_TOKEN("JWT_001", "잘못된 JWT 토큰입니다."),
    JWT_UNSUPPORTED("JWT_002", "미지원 JWT 토큰 방식입니다."),
    JWT_EXPIRED("JWT_003", "만료된 토큰입니다."),
    /* JWT 오류 메세지 정의 끝 */

    /* 카카오톡 인증 오류 메세지 정의 */
    AUTH_KAKAO_UNKNOWN_ERROR("KKO_000", "알 수 없는 카카오 통신 모듈 오류입니다."),
    AUTH_KAKAO_NOT_SUPPORTED_METHOD("KKO_001", "지원하지 않는 인증 방식 입니다."),
    AUTH_KAKAO_NOT_REGISTERED_USER("KKO_002", "등록되지 않은 카카오 유저입니다."),
    /* 카카오톡 인증 오류 메세지 정의 끝 */

    ETC("ETC", "");

    private ErrorResponse errorResponse;

    ErrorEnum(String errCode, String message) {
        this.errorResponse = new ErrorResponse(errCode, message);
    }

    @Getter
    public class ErrorResponse {
        private String errCode;
        private String message;

        public ErrorResponse(String errCode, String message) {
            this.errCode = errCode;
            this.message = message;
        }
    }
}
