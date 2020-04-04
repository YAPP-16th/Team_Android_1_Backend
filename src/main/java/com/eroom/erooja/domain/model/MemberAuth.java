package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.features.auth.kakao.json.KakaoUserJSON;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MemberAuth extends AuditProperties {
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String thirdPartyProvider;

    private String thirdPartyUserInfo;

    @Column(nullable = false)
    private Boolean isThirdParty;

    @OneToOne(cascade = CascadeType.ALL)
    private Members member;
}
