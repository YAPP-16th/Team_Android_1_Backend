package com.yapp.erooja.feature.auth.kakao.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoProfileJSON {
    private String nickname;

    @JsonIgnore
    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @JsonIgnore
    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;
}
