package com.eroom.erooja.features.auth.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAuthDTO {
    private Long kakaoId;
    private String accessToken;
}
