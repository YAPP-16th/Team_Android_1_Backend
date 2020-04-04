package com.eroom.erooja.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    ROLE_USER, ROLE_DEVELOPER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return "" + this;
    }
}
