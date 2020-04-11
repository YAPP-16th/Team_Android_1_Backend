package com.eroom.erooja.features.interest.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobInterestService {
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

    public void setUpDefaultJobInterests() {
        long primeJobInterestId = 1L;
        String[] jobInterests_develop = {
                "서버", "프론트엔드", "안드로이드", "iOS", "Data Engineer", "Data Scientist",
                "DevOps", "머신 러닝", "게임, 애니메이션"
        };

        String[] jobInterests_design = {
                "UX 디자인", "UI, GUI 디자인", "영상, 모션 디자인", "모바일 디자인", "편집 디자인",
                "그래픽 디자인", "웹 디자인", "BX 디자인", "제품 디자인"
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

        jobInterestRepository.save(jobGroup_develop);
        jobInterestRepository.save(jobGroup_design);

        for(String name : jobInterests_develop) {
            JobInterest interest = JobInterest.builder()
                    .id(primeJobInterestId++)
                    .name(name)
                    .jobGroup(jobGroup_develop)
                    .jobInterestType(JobInterestType.JOB_INTEREST)
                    .build();

            jobInterestRepository.save(interest);
        }

        for(String name : jobInterests_design) {
            JobInterest interest = JobInterest.builder()
                    .id(primeJobInterestId++)
                    .name(name)
                    .jobGroup(jobGroup_design)
                    .jobInterestType(JobInterestType.JOB_INTEREST)
                    .build();
            jobInterestRepository.save(interest);
        }
    }
}
