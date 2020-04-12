package com.eroom.erooja.features.member.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.MemberAuth;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.domain.repos.MemberAuthRepository;
import com.eroom.erooja.domain.repos.MemberRepository;
import com.eroom.erooja.features.auth.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;

    public Members findById(String uid) {
        Optional<Members> member = memberRepository.findById(uid);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new EroojaException(ErrorEnum.MEMBER_NOT_FOUND);
        }
    }

    @Transactional
    public Members updateNotNullPropsOf(Members member) {
        Members updated = memberRepository.save(member);
        MemberAuth memberAuth = memberAuthRepository.getOne(member.getUid());

        if (memberAuth.isAdditionalInfoNeeded()) {
            memberAuth.setAdditionalInfoNeeded(updated.isAdditionalInfoNeeded());

            memberAuth = memberAuthRepository.save(memberAuth);
        }

        return memberAuth.getMember();
    }

    public Boolean isNicknameExist(String nickname) {
        String trimmedNickname = nickname.trim();
        return memberRepository.existsByNickname(trimmedNickname);
    }
}
