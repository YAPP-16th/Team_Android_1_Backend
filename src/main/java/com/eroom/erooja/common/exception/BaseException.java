package com.eroom.erooja.common.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends Exception {
    protected HttpStatus status;
    protected ErrorEnum errorEnum;

    public BaseException(HttpStatus status, ErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.status = status;
        this.errorEnum = errorEnum;
    }
}
