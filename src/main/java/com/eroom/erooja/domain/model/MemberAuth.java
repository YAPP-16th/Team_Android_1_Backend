package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.domain.enums.MemberRole;
import com.eroom.erooja.domain.enums.AuthProvider;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberAuth extends AuditProperties implements UserDetails {
    @Id
    private String uid;

    private String password;

    private AuthProvider authProvider;

    private String thirdPartyUserInfo;

    private boolean isAdditionalInfoNeeded = true;

    private boolean isThirdParty = true;

    private boolean isEnabled = true;

    private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isCredentialsNonExpired = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> authorities;

    @Setter
    @OneToOne(mappedBy = "memberAuth", cascade = CascadeType.ALL)
    private Members member;

    @Override
    public String getUsername() {
        return uid;
    }

    public static String generateUid(AuthProvider authProvider, String thirdPartyIdentifier) {
        return authProvider + "@" + thirdPartyIdentifier;
    }

    @Builder
    public MemberAuth(String uid, String password, Members member, Set<MemberRole> authorities,
                      AuthProvider authProvider, String thirdPartyUserInfo, boolean isThirdParty) {
        this.uid = uid;
        this.password = password;
        this.member = member;
        this.authorities = authorities;
        this.authProvider = authProvider;
        this.thirdPartyUserInfo = thirdPartyUserInfo;
        this.isThirdParty = isThirdParty;
    }
}
