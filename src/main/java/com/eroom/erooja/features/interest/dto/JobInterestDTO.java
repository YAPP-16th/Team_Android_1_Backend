package com.eroom.erooja.features.interest.dto;

import com.eroom.erooja.domain.model.JobInterest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobInterestDTO {
    private Long id;
    private String name;
    private Long jobGroupId;

    public JobInterestDTO(JobInterest jobInterest) {
        this.id = jobInterest.getId();
        this.name = jobInterest.getName();

        if (jobInterest.getJobGroup() == null) {
            this.jobGroupId = null;
        } else {
            this.jobGroupId = jobInterest.getJobGroup().getId();
        }
    }
}
