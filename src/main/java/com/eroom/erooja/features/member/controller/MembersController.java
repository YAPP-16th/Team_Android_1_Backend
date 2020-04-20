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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
}
