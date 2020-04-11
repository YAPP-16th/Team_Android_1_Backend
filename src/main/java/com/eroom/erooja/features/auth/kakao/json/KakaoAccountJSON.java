package com.eroom.erooja.features.auth.kakao.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoAccountJSON {
    private Boolean profileNeedsAgreement;

    private KakaoProfileJSON profile;

    private Boolean emailNeedsAgreement;

    private String email;

    private Boolean ageRangeNeedsAgreement;

    private String ageRange;

    private Boolean birthdayNeedsAgreement;

    private String birthday;

    private String birthdayType;

    private Boolean genderNeedsAgreement;

    private String gender;
}
