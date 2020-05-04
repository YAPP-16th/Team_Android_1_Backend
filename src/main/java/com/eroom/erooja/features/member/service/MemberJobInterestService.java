package com.eroom.erooja.features.member.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import com.eroom.erooja.domain.repos.MemberJobInterestRepository;
import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberJobInterestService {
    private final JobInterestRepository jobInterestRepository;
    private final JobInterestService jobInterestService;
    private final MemberJobInterestRepository memberJobInterestRepository;

    public List<JobInterest> getJobGroupList(String uid) {
        List<MemberJobInterest> interests
                = memberJobInterestRepository.getAllByMember_UidAndJobInterest_JobInterestType(uid, JobInterestType.JOB_GROUP);

        return interests.stream().map(MemberJobInterest::getJobInterest).collect(Collectors.toList());
    }

    public List<JobInterest> getJobInterestsByUidAndJobGroup(String uid, Long jobGroupId) {
        List<MemberJobInterest> memberJobInterests
                = memberJobInterestRepository.getAllByMember_UidAndJobInterest_JobGroup_Id(uid, jobGroupId);

        return memberJobInterests.stream().map(MemberJobInterest::getJobInterest).collect(Collectors.toList());
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
        JobInterest interest = jobInterestService.findById(jobInterestId);
        Members member = Members.builder().uid(uid).build();

        if (interest.getJobInterestType().equals(JobInterestType.JOB_INTEREST) &&
                !existsByUidAndJobInterestId(uid, interest.getJobGroup().getId())) {
            JobInterest jobGroup = jobInterestService.findById(interest.getJobGroup().getId());

            memberJobInterestRepository.save(
                    MemberJobInterest.builder()
                            .jobInterest(jobGroup)
                            .member(member)
                        .build());
        }

        return memberJobInterestRepository.save(
                MemberJobInterest.builder()
                        .jobInterest(interest)
                        .member(member)
                    .build());
    }

    public Integer addJobInterestListForUid(String uid, List<Long> ids) {
        int savedCount = 0;
        for(Long id : ids) {
            if (!jobInterestRepository.existsById(id)) continue;
            if (memberJobInterestRepository.existsByMember_UidAndJobInterest_Id(uid, id)) continue;
            if (this.addJobInterestForUid(uid, id) != null) savedCount += 1;
        }

        return savedCount;
    }

    public Map<String, List<JobInterest>> getJobInterestsByUids(List<String> uids) {
        Map<String, List<JobInterest>> jobInterestsByUid = new HashMap<>();

        uids.forEach(uid -> {
            List<MemberJobInterest> memberJobInterest = memberJobInterestRepository.getAllByMember_Uid(uid);
            jobInterestsByUid.put(uid, memberJobInterest.stream().map(MemberJobInterest::getJobInterest).collect(Collectors.toList()));
        });

        return jobInterestsByUid;
    }

    public void deleteByUidAndJobInterestId(String uid, Long jobInterestId) {
        MemberJobInterest memberJobInterest = memberJobInterestRepository.getByMember_UidAndJobInterest_Id(uid, jobInterestId);

        if (memberJobInterest == null) {
            throw new EroojaException(ErrorEnum.JOB_INTEREST_NOT_EXISTS);
        }

        memberJobInterestRepository.delete(memberJobInterest);
    }
}
