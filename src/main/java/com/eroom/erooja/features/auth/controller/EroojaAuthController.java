package com.eroom.erooja.features.auth.controller;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.MemberAuth;
import com.eroom.erooja.features.auth.erooja.dto.AuthDTO;
import com.eroom.erooja.features.auth.jwt.JwtResponse;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.auth.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/erooja")
@RequiredArgsConstructor
public class EroojaAuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberAuthService memberAuthService;

    @PostMapping
    public ResponseEntity<JwtResponse> authenticate(@RequestBody AuthDTO authDTO) {

        MemberAuth memberAuth = (MemberAuth) memberAuthService.loadUserByUsername(authDTO.getUid());

        if (!memberAuth.getPassword().equals(authDTO.getPassword())) {
            throw new EroojaException(ErrorEnum.AUTH_ACCESS_DENIED);
        }

        return ResponseEntity.ok(JwtResponse.builder()
                .token(jwtTokenProvider.generateToken(memberAuth))
                .refreshToken(jwtTokenProvider.generateRefreshToken(memberAuth))
                .isAdditionalInfoNeeded(memberAuth.isAdditionalInfoNeeded())
                .build());
    }
}
