package com.eroom.erooja.features.member.controller;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.member.dto.MemberDTO;
import com.eroom.erooja.features.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MembersController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity getMemberByUid(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header) {
        String uid = jwtTokenProvider.getUidFromHeader(header);
        return ResponseEntity.ok(MemberDTO.of(memberService.findById(uid)));
    }

    @PostMapping
    public ResponseEntity updateMemberByUidAndToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                                    @RequestBody MemberDTO memberDTO) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        Members updated = memberService.updateNotNullPropsOf(Members.of(uid, memberDTO));
        return ResponseEntity.ok(MemberDTO.of(updated));
    }

    @PutMapping("/nickname")
    public ResponseEntity updateNicknameByUid(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                              @RequestBody MemberDTO memberDTO) throws EroojaException {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        if (memberService.isNicknameExist(memberDTO.getNickname())) {
            throw new EroojaException(ErrorEnum.MEMBER_DUPLICATED_PROPS);
        }

        Members updated = memberService.updateNotNullPropsOf(Members.of(uid, memberDTO));

        return ResponseEntity.ok(MemberDTO.of(updated));
    }

    @PostMapping("/nickname/duplicity")
    public ResponseEntity checkNicknameDuplicity(@RequestBody MemberDTO memberDTO) throws EroojaException {
        if (memberService.isNicknameExist(memberDTO.getNickname().trim())) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping(value = "/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity uploadAndUpdateImage(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                               @RequestBody MultipartFile multipartImageFile) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        Members member = memberService.updateProfilePicture(uid, multipartImageFile);
        return ResponseEntity.ok(MemberDTO.of(member));
    }

    @GetMapping("/images")
    public ResponseEntity getSavedImage(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        return ResponseEntity.ok(memberService.getSavedImagePaths(uid));
    }

    @DeleteMapping("/images")
    public ResponseEntity deleteImage(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                      @RequestBody List<String> imagePaths) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        for (String path : imagePaths) {
            if (!path.contains(uid)) {
                throw new EroojaException(ErrorEnum.MEMBER_IMAGE_DELETE_UNAUTHORIZED);
            }
        }

        memberService.deleteSavedImages(imagePaths);

        return ResponseEntity.ok().build();
    }
}
