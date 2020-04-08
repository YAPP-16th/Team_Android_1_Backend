package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.model.JobInterest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;

@Getter
public class JobInterestSetListDTO extends RepresentationModel<JobInterestSetListDTO> {
    List<Set<JobInterest>> jobInterests;

    @JsonCreator
    public JobInterestSetListDTO(@JsonProperty List<Set<JobInterest>> jobInterests) {
        this.jobInterests = jobInterests;
    }
}
