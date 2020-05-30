package com.eroom.erooja.common.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.features.goal.exception.GoalNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ EroojaException.class })
    public void handleEroojaException(HttpServletRequest request, HttpServletResponse response, EroojaException ex) throws IOException {
        ErrorEnum errorEnum = ex.getErrorEnum();
        ErrorEnum.ErrorResponse errorResponse = errorEnum.getErrorResponse();
        response.sendError(ex.getStatus().value(), errorResponse.getMessage());

        switch (errorEnum) {
            default:
                logger.error("Erooja 오류 감지. - Enum : {}", errorEnum, ex);
            case GOAL_NOT_FOUND:
            case GOAL_JOIN_NOT_FOUND:
                response.sendError(ex.getStatus().value(), errorResponse.getMessage());
        }
    }

    /* 외부 라이브러리 예외 처리 정의 */
    @ExceptionHandler({ JwtException.class })
    public void handleJwtException(HttpServletRequest request, HttpServletResponse response, JwtException ex) throws IOException {
        logger.warn("잘못된 JWT 감지", ex);

        if ((ex instanceof ExpiredJwtException)) {
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_EXPIRED));
        } else if (ex instanceof MalformedJwtException) {
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_MALFORMED_TOKEN));
        } else if (ex instanceof UnsupportedJwtException) {
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_UNSUPPORTED));
        } else {
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_UNKNOWN_ERROR));
        }
    }
    /* 외부 라이브러리 예외 처리 정의 끝 */

    @ExceptionHandler({ Exception.class })
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        ex.printStackTrace();

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");
        logger.error("알 수 없는 오류 감지.", ex);
    }
}
