package com.yapp.erooja.features.auth.kakao.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
