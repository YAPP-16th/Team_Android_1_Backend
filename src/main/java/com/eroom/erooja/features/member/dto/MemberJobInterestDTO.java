package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class MemberJobInterestDTO extends RepresentationModel<MemberJobInterestDTO> {
    private String uid;
    private String name;
    private JobInterestType jobInterestType;

    @JsonCreator
    public MemberJobInterestDTO(String uid, String name, JobInterestType jobInterestType) {
        this.uid = uid;
        this.name = name;
        this.jobInterestType = jobInterestType;
    }

    public static MemberJobInterestDTO of(MemberJobInterest memberJobInterest) {
        return new MemberJobInterestDTO(
                memberJobInterest.getMember().getUid(),
                memberJobInterest.getJobInterest().getName(),
                memberJobInterest.getJobInterest().getJobInterestType());
    }
}
