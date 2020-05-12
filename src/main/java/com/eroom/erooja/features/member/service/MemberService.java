package com.eroom.erooja.features.member.service;

import com.eroom.erooja.common.component.AwsClient;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AwsClient awsClient;
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
    public Members updateNotNullPropsOf(Members toBe) {
        Members origin = memberRepository.getOne(toBe.getUid());

        Members updated = memberRepository.save(Members.builder()
                .uid(toBe.getUid())
                .nickname(toBe.getNickname() == null ? origin.getNickname() : toBe.getNickname())
                .imagePath(toBe.getImagePath() == null ? origin.getImagePath() : toBe.getImagePath())
            .build()
        );
        MemberAuth memberAuth = memberAuthRepository.getOne(toBe.getUid());

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

    @Transactional
    public Members updateProfilePicture(String uid, MultipartFile multipartFile) {
        Members member = memberRepository.getOne(uid);

        String imagePath = awsClient.uploadFile(uid, multipartFile);

        return memberRepository.save(
                Members.builder()
                        .uid(member.getUid())
                        .nickname(member.getNickname())
                        .memberAuth(member.getMemberAuth())
                        .imagePath(imagePath)
                    .build()
        );
    }

    public List<String> getSavedImagePaths(String uid) {
        return awsClient.listUpFilePath(uid);
    }

    public void deleteSavedImages(List<String> imagePaths) {
        imagePaths.forEach(awsClient::deleteFileFromS3Bucket);
    }
}
