package com.eroom.erooja.features.interest.dto;

import com.eroom.erooja.domain.model.JobInterest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class JobGroupAndInterestsDTO {
    private Long id;
    private String name;
    private List<JobInterestDTO> jobInterests;

    public JobGroupAndInterestsDTO(JobInterest jobGroup, List<JobInterest> jobInterests) {
        this.id = jobGroup.getId();
        this.name = jobGroup.getName();
        this.jobInterests = jobInterests.stream()
                .map(JobInterestDTO::new)
                .collect(Collectors.toList());
    }
}
