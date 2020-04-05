package com.eroom.erooja.common.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ JwtException.class })
    public ResponseEntity<ErrorEnum.ErrorResponse> handleJwtException(JwtException ex) {
        if (ex instanceof MalformedJwtException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEnum.JWT_MALFORMED_TOKEN.getErrorResponse());
        } else if (ex instanceof UnsupportedJwtException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEnum.JWT_UNSUPPORTED.getErrorResponse());
        } else {
            logger.error("JWT Error invoked - message : {}, cause : {}", ex.getMessage(), ex.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEnum.JWT_UNKNOWN_ERROR.getErrorResponse());
        }
    }

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<ErrorEnum.ErrorResponse> handleNotRegisteredUserException(BaseException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorEnum().getErrorResponse());
    }
}
