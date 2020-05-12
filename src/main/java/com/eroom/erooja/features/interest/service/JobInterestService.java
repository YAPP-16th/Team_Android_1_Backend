package com.eroom.erooja.features.interest.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobInterestService {
    private final static Logger logger = LoggerFactory.getLogger(JobInterestService.class);

    private final JobInterestRepository jobInterestRepository;

    public JobInterest findById(Long id) {
        JobInterest jobInterest = jobInterestRepository.findJobInterestById(id);

        if (jobInterest == null) {
            throw new EroojaException(ErrorEnum.JOB_INTEREST_NOT_EXISTS);
        }

        return jobInterest;
    }

    public List<JobInterest> findJobGroups() {
        return jobInterestRepository.findJobInterestsByJobInterestType(JobInterestType.JOB_GROUP);
    }

    public List<JobInterest> findByJobGroup_id(Long id) {
        return jobInterestRepository.findJobInterestsByJobGroup_Id(id);
    }


    public JobInterest addJobGroup(String name) {
        return jobInterestRepository.save(
                JobInterest.builder()
                        .name(name)
                        .jobInterestType(JobInterestType.JOB_GROUP)
                        .jobGroup(null)
                    .build()
        );
    }

    public JobInterest addJobInterest(Long jobGroupId, String interestName) {
        JobInterest jobGroup = findById(jobGroupId);

        return jobInterestRepository.save(
                JobInterest.builder()
                            .name(interestName)
                            .jobInterestType(JobInterestType.JOB_INTEREST)
                            .jobGroup(jobGroup)
                        .build()
        );
    }

    public List<JobInterest> setUpDefaultJobInterests() {
        jobInterestRepository.deleteAll();

        Long primeJobInterestId = 1L;
        String[] jobInterests_develop = {
                "서버", "프론트엔드", "안드로이드", "iOS", "Data Engineer", "Data Scientist",
                "DevOps", "머신 러닝", "게임/애니메이션"
        };

        String[] jobInterests_design = {
                "UX 디자인", "UI/GUI 디자인", "웹 디자인", "그래픽 디자인",
                "산업 디자인", "편집 디자인", "영상/모션 디자인", "BX 디자인"
        };

        JobInterest jobGroup_develop = JobInterest.builder()
                .id(primeJobInterestId++)
                .name("개발")
                .jobGroup(null)
                .jobInterestType(JobInterestType.JOB_GROUP)
                .build();

        JobInterest jobGroup_design = JobInterest.builder()
                .id(primeJobInterestId++)
                .name("디자인")
                .jobGroup(null)
                .jobInterestType(JobInterestType.JOB_GROUP)
                .build();

        jobGroup_develop = jobInterestRepository.save(jobGroup_develop);
        jobGroup_design = jobInterestRepository.save(jobGroup_design);

        List<JobInterest> jobInterests = new ArrayList<>();

        jobInterests.addAll(saveAllJobInterests(primeJobInterestId, jobGroup_develop, jobInterests_develop));
        jobInterests.addAll(saveAllJobInterests(primeJobInterestId, jobGroup_design, jobInterests_design));

        return jobInterests;
    }

    private List<JobInterest> saveAllJobInterests(long primeJobInterestId, JobInterest jobGroup, String[] jobInterests) {
        List<JobInterest> jobInterestsList = new ArrayList<>();

        for(String name : jobInterests) {
            JobInterest interest = JobInterest.builder()
                    .id(primeJobInterestId++)
                    .name(name)
                    .jobGroup(jobGroup)
                    .jobInterestType(JobInterestType.JOB_INTEREST)
                    .build();

            jobInterestRepository.save(interest);
        }

        return jobInterestsList;
    }
}
