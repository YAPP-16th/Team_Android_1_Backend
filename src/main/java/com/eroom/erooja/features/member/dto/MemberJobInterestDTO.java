package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.model.MemberJobInterest;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class MemberJobInterestDTO extends RepresentationModel<MemberJobInterestDTO> {
    private String uid;
    private String name;
    private int level;

    @JsonCreator
    public MemberJobInterestDTO(String uid, String name, int level) {
        this.uid = uid;
        this.name = name;
        this.level = level;
    }

    public static MemberJobInterestDTO of(MemberJobInterest memberJobInterest) {
        return new MemberJobInterestDTO(
                memberJobInterest.getMember().getUid(),
                memberJobInterest.getJobInterest().getName(),
                memberJobInterest.getJobInterest().getLevel());
    }
}
