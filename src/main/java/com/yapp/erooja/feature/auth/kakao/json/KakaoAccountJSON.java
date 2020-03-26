package com.yapp.erooja.feature.auth.kakao.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAccountJSON {
    @JsonIgnore
    @JsonProperty("profile_needs_agreement")
    private Boolean profileNeedsAgreement;

    private KakaoProfileJSON profile;

    @JsonIgnore
    @JsonProperty("email_needs_agreement")
    private Boolean emailNeedsAgreement;

    private String email;

    @JsonIgnore
    @JsonProperty("age_range_needs_agreement")
    private Boolean ageRangeNeedsAgreement;

    @JsonIgnore
    @JsonProperty("age_range")
    private String ageRange;

    @JsonIgnore
    @JsonProperty("birthday_needs_agreement")
    private Boolean birthdayNeedsAgreement;

    private String birthday;

    @JsonIgnore
    @JsonProperty("birthday_type")
    private String birthdayType;

    @JsonIgnore
    @JsonProperty("gender_needs_agreement")
    private Boolean genderNeedsAgreement;

    private String gender;

}
