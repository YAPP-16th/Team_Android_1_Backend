package com.eroom.erooja.features.member.service;

import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.domain.repos.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Members> findById(String uid) {
        return memberRepository.findById(uid);
    }

    public Members updateNotNullPropsOf(Members member) {
        return memberRepository.save(member);
    }

    public Boolean isNicknameExist(String nickname) {
        String trimmedNickname = nickname.trim();
        return memberRepository.existsByNickname(trimmedNickname);
    }

    public Members updateNickname(String uid, String nickname) {
        return updateNotNullPropsOf(Members.builder().uid(uid).nickname(nickname).build());
    }
}
