package com.yapp.erooja.feature.auth.kakao.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoIdsJSON {
    List<Long> elements;

    Integer totalCount;

    Integer beforeUrl;

    Integer afterUrl;
}
