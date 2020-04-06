package com.eroom.erooja.common.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EroojaException extends RuntimeException {
    protected HttpStatus status;
    protected ErrorEnum errorEnum;

    public EroojaException(ErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.status = errorEnum.getErrorResponse().getHttpStatus();
        this.errorEnum = errorEnum;
    }
}
