package com.eroom.erooja.features.auth.kakao.exception;

import com.eroom.erooja.common.exception.BaseException;
import com.eroom.erooja.common.constants.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KakaoRESTException extends BaseException {
    public KakaoRESTException(HttpStatus status, ErrorEnum errorEnum) {
        super(status, errorEnum);
    }
}
