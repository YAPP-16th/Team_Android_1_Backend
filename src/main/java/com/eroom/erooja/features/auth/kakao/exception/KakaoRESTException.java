package com.eroom.erooja.features.auth.kakao.exception;

import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.common.constants.ErrorEnum;
import lombok.Getter;

@Getter
public class KakaoRESTException extends EroojaException {
    public KakaoRESTException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
