package com.eroom.erooja.common.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
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
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ EroojaException.class })
    public void handleEroojaException(HttpServletRequest request, HttpServletResponse response, EroojaException ex) throws IOException {
        ErrorEnum.ErrorResponse errorResponse = ex.getErrorEnum().getErrorResponse();
        response.sendError(ex.getStatus().value(), errorResponse.getMessage());
    }

    /* 외부 라이브러리 예외 처리 정의 */
    @ExceptionHandler({ JwtException.class })
    public void handleJwtException(HttpServletRequest request, HttpServletResponse response, JwtException ex) throws IOException {
        if (ex instanceof MalformedJwtException) {
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_MALFORMED_TOKEN));
        } else if (ex instanceof UnsupportedJwtException) {
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_UNSUPPORTED));
        } else {
            logger.error("JWT Error invoked - message : {}, cause : {}", ex.getMessage(), ex.getCause());
            handleEroojaException(request, response, new EroojaException(ErrorEnum.JWT_UNKNOWN_ERROR));
        }
    }
    /* 외부 라이브러리 예외 처리 정의 끝 */


}
