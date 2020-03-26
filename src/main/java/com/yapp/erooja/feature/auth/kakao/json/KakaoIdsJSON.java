package com.yapp.erooja.feature.auth.kakao.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KakaoIdsJSON {
    List<String> elements;

    @JsonIgnore
    @JsonProperty(value = "total_count")
    Integer totalCount;

    @JsonIgnore
    @JsonProperty(value = "before_url")
    Integer beforUrl;

    @JsonIgnore
    @JsonProperty(value = "after_url")
    Integer afterUrl;
}
