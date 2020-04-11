package com.eroom.erooja.features.member.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import com.eroom.erooja.domain.repos.MemberJobInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberJobInterestService {
    private final JobInterestRepository jobInterestRepository;
    private final MemberJobInterestRepository memberJobInterestRepository;

    public List<JobInterest> getJobGroupList(String uid) {
        List<MemberJobInterest> interests
                = memberJobInterestRepository.getAllByMember_UidAndJobInterest_JobInterestType(uid, JobInterestType.JOB_GROUP);

        return interests.stream().map(MemberJobInterest::getJobInterest).collect(Collectors.toList());
    }

    public List<JobInterest> getJobInterestsByUidAndJobGroup(String uid, Long jobGroupId) {
        List<MemberJobInterest> interests
                = memberJobInterestRepository.getAllByMember_UidAndJobInterest_JobInterestType(uid, JobInterestType.JOB_INTEREST);

        return interests.stream()
                .map(MemberJobInterest::getJobInterest)
                .filter(interest -> interest.getJobGroup().getId().equals(jobGroupId))
                .collect(Collectors.toList());
    }

    public Boolean existsByUidAndJobInterestId(String uid, Long jobInterestId) {
        if (!jobInterestRepository.existsById(jobInterestId)) {
            throw new EroojaException(ErrorEnum.JOB_INTEREST_NOT_EXISTS);
        }

        return memberJobInterestRepository.existsByMember_UidAndJobInterest_Id(uid, jobInterestId);
    }

    public MemberJobInterest getByUidAndJobInterestId(String uid, Long jobInterestId) {
        return memberJobInterestRepository.getByMember_UidAndJobInterest_Id(uid, jobInterestId);
    }

    public MemberJobInterest addJobInterestForUid(String uid, Long jobInterestId) {
       return memberJobInterestRepository.save(MemberJobInterest.builder()
                .jobInterest(JobInterest.builder().id(jobInterestId).build())
                .member(Members.builder().uid(uid).build())
            .build()
        );
    }
}