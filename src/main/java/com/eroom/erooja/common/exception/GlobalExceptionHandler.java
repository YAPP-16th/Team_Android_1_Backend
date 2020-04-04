package com.eroom.erooja.common.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<ErrorEnum.ErrorResponse> handleNotRegisteredUserException(BaseException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorEnum().getErrorResponse());
    }
}
