package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.interest.dto.JobInterestDTO;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWithJobInterestsDTO {
    @Setter
    private String uid;
    private String nickname;
    private String imagePath;
    private List<JobInterestDTO> jobInterests;

    public static MemberWithJobInterestsDTO of(Members members, List<JobInterest> jobInterests) {
        return MemberWithJobInterestsDTO.builder()
                    .uid(members.getUid())
                    .nickname(members.getNickname())
                    .imagePath(members.getImagePath())
                    .jobInterests(
                            jobInterests.stream()
                                    .filter(ji -> ji.getJobInterestType().equals(JobInterestType.JOB_INTEREST))
                                    .map(JobInterestDTO::new)
                                    .collect(Collectors.toList()))
                .build();
    }

}
