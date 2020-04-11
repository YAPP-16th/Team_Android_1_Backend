package com.eroom.erooja.features.interest.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
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
        return jobInterestRepository.findJobInterestsByLevel(JobInterest.ROOT_LEVEL);
    }

    public List<JobInterest> findByJobGroup_id(Long id) {
        return jobInterestRepository.findJobInterestsByJobGroup_Id(id);
    }


    public JobInterest addJobGroup(String name) {
        return jobInterestRepository.save(
                JobInterest.builder()
                        .name(name)
                        .level(JobInterest.ROOT_LEVEL)
                        .jobGroup(null)
                    .build()
        );
    }

    public JobInterest addJobInterest(Long jobGroupId, String interestName) {
        JobInterest jobGroup = findById(jobGroupId);

        return jobInterestRepository.save(
                JobInterest.builder()
                            .name(interestName)
                            .level(JobInterest.MAX_LEVEL)
                            .jobGroup(jobGroup)
                        .build()
        );
    }
}
