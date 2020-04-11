package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.model.JobInterest;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class HasJobInterestDTO extends RepresentationModel<HasJobInterestDTO> {
    private String uid;
    private Boolean hasJobInterest;
    private JobInterest jobInterest;

    @JsonCreator
    public HasJobInterestDTO(String uid, Boolean hasJobInterest, JobInterest jobInterest) {
        this.uid = uid;
        this.hasJobInterest = hasJobInterest;
        this.jobInterest = jobInterest;
    }
}
