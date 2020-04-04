package com.eroom.erooja.features.auth.kakao.controller;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.domain.enums.AuthProvider;
import com.eroom.erooja.domain.model.MemberAuth;
import com.eroom.erooja.features.auth.jwt.JwtResponse;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.auth.kakao.dto.KakaoAuthDTO;
import com.eroom.erooja.features.auth.kakao.dto.KakaoAuthMethod;
import com.eroom.erooja.features.auth.kakao.exception.KakaoRESTException;
import com.eroom.erooja.features.auth.kakao.json.KakaoUserJSON;
import com.eroom.erooja.features.auth.kakao.service.KakaoUserRESTService;
import com.eroom.erooja.features.auth.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/kakao")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KakaoAuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserRESTService kakaoUserRESTService;
    private final MemberAuthService memberAuthService;

    @PostMapping
    public ResponseEntity<JwtResponse> authenticate(@RequestParam KakaoAuthMethod by,
                                          @RequestBody KakaoAuthDTO kakaoAuthDTO) throws Exception {
        KakaoUserJSON kakaoUser;

        if (by.equals(KakaoAuthMethod.ACCESS_TOKEN)) kakaoUser = kakaoUserRESTService.findUserByToken(kakaoAuthDTO.getAccessToken());
        else if (by.equals(KakaoAuthMethod.ID)) kakaoUser = kakaoUserRESTService.findUserById(kakaoAuthDTO.getKakaoId());
        else throw new KakaoRESTException(HttpStatus.BAD_REQUEST, ErrorEnum.AUTH_KAKAO_NOT_SUPPORTED_METHOD);

        MemberAuth memberAuth;
        try {
            String uid = MemberAuth.generateUid(AuthProvider.KAKAO, kakaoUser.getId().toString());
            memberAuth = (MemberAuth) memberAuthService.loadUserByUsername(uid);
        } catch (UsernameNotFoundException nfe) {
            memberAuth = memberAuthService.create(kakaoUser);
        }

        return ResponseEntity.ok(JwtResponse.builder().token(jwtTokenProvider.generateToken(memberAuth)).build());
    }
}
