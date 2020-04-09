package com.eroom.erooja.features.member.controller;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.member.dto.HasJobInterestDTO;
import com.eroom.erooja.features.member.dto.JobInterestDTO;
import com.eroom.erooja.features.member.dto.JobInterestSetListDTO;
import com.eroom.erooja.features.member.dto.MemberJobInterestDTO;
import com.eroom.erooja.features.member.service.MemberJobInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/member/jobInterest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberJobInterestController {
    private final MemberJobInterestService memberJobInterestService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity getJobInterests(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        List<Set<JobInterest>> jobInterests = memberJobInterestService.getJobInterestSetListByLevelAndUid(uid);

        JobInterestSetListDTO jobInterestSetListDTO = new JobInterestSetListDTO(jobInterests);

        return ResponseEntity.ok(jobInterestSetListDTO);
    }

    @PutMapping
    public ResponseEntity addJobInterest(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                         @RequestBody @Valid JobInterestDTO jobInterestDTO,
                                         BindingResult bindingResult) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        if (bindingResult.hasErrors()) {
            throw new EroojaException(ErrorEnum.MEMBER_JOB_INTEREST_INVALID_BODY);
        }

        if (memberJobInterestService.existsByUidAndJobInterestId(uid, jobInterestDTO.getJobInterestId())) {
            throw new EroojaException(ErrorEnum.MEMBER_JOB_INTEREST_ALREADY_EXISTS);
        }

        MemberJobInterest memberJobInterest = memberJobInterestService.addJobInterestForUid(uid, jobInterestDTO.getJobInterestId());
        return ResponseEntity.ok(MemberJobInterestDTO.of(memberJobInterest));
    }

    @GetMapping("/has/{jobInterestId}")
    public ResponseEntity hasJobInterest(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                         @PathVariable("jobInterestId") Long jobInterestId) {
        String uid = jwtTokenProvider.getUidFromHeader(header);

        Boolean hasOne = memberJobInterestService.existsByUidAndJobInterestId(uid, jobInterestId);

        HasJobInterestDTO hasJobInterestDTO;
        if (hasOne) {
            MemberJobInterest memberJobInterest = memberJobInterestService.getByUidAndJobInterestId(uid, jobInterestId);
            hasJobInterestDTO = new HasJobInterestDTO(uid, true, memberJobInterest.getJobInterest());
            return ResponseEntity.ok(hasJobInterestDTO);
        } else {
            hasJobInterestDTO = new HasJobInterestDTO(uid, false, null);
            return ResponseEntity.ok(hasJobInterestDTO);
        }
    }

}
