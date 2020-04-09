package com.eroom.erooja.features.auth.service;


import com.eroom.erooja.domain.enums.AuthProvider;
import com.eroom.erooja.domain.enums.MemberRole;
import com.eroom.erooja.domain.model.MemberAuth;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.domain.repos.MemberAuthRepository;
import com.eroom.erooja.domain.repos.MemberRepository;
import com.eroom.erooja.features.auth.kakao.json.KakaoUserJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberAuthService implements UserDetailsService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        Optional<MemberAuth> memberAuthInfo = memberAuthRepository.findById(uid);
        if (!memberAuthInfo.isPresent()) {
            throw new UsernameNotFoundException("해당하는 멤버를 찾을 수 없습니다. - uid : " + uid);
        }

        return memberAuthInfo.get();
    }

    @Transactional
    public MemberAuth create(KakaoUserJSON kakaoUserJSON) throws JsonProcessingException {
        String uid = MemberAuth.generateUid(AuthProvider.KAKAO, kakaoUserJSON.getId().toString());
        Map<String, String> properties = kakaoUserJSON.getProperties();

        properties.putIfAbsent("nickname", "닉네임");
        properties.putIfAbsent("imagePath", null);

        Members member = Members.builder()
                .uid(uid)
                .nickname(properties.get("nickname"))
                .imagePath(properties.get("imagePath"))
                .build();

        MemberAuth build = MemberAuth.builder()
                .uid(uid)
                .member(member)
                .authProvider(AuthProvider.KAKAO)
                .isThirdParty(true)
                .authorities(Collections.singleton(MemberRole.ROLE_USER))
                .password("")
                .thirdPartyUserInfo(objectMapper.writeValueAsString(kakaoUserJSON))
            .build();

        member.setMemberAuth(build);

        MemberAuth savedMemberAuth = memberAuthRepository.save(build);

        return savedMemberAuth;
    }
}
