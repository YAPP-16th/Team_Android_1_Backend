package com.yapp.erooja.feature.auth.kakao.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserJSON {
    private String id;
    private String properties;

    @JsonIgnore
    @JsonProperty(value = "kakao_account")
    private KakaoAccountJSON account;
}
