package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.model.JobInterest;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
public class JobInterestDTO extends RepresentationModel<JobInterestDTO> {
    private Long jobInterestId;

    private String name;

    @Min(JobInterest.ROOT_LEVEL) @Max(JobInterest.MAX_LEVEL)
    private int level;
}
