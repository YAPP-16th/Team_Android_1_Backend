package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberGoal;
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
    private GoalRole role;
    private String imagePath;
    private List<JobInterestDTO> jobInterests;

    public static MemberWithJobInterestsDTO of(MemberGoal memberGoal, List<JobInterest> jobInterests) {
        Members members = memberGoal.getMember();

        return MemberWithJobInterestsDTO.builder()
                    .uid(members.getUid())
                    .nickname(members.getNickname())
                    .role(memberGoal.getRole())
                    .imagePath(members.getImagePath())
                    .jobInterests(
                            jobInterests.stream()
                                    .filter(ji -> ji.getJobInterestType().equals(JobInterestType.JOB_INTEREST))
                                    .map(JobInterestDTO::new)
                                    .collect(Collectors.toList()))
                .build();
    }

}
