package com.eroom.erooja.features.member.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
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

    public Members findById(String uid) {
        Optional<Members> member = memberRepository.findById(uid);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new EroojaException(ErrorEnum.MEMBER_NOT_FOUND);
        }
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
