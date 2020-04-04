package com.eroom.erooja.features.auth.kakao.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import org.springframework.http.HttpStatus;

public class KakaoNotRegisteredUserException extends KakaoRESTException {
    public KakaoNotRegisteredUserException() {
        super(HttpStatus.NOT_FOUND, ErrorEnum.AUTH_KAKAO_NOT_REGISTERED_USER);
    }
}
