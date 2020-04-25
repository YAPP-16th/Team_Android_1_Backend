package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinimalGoalDetailDTO {
    private Long id;

    private String title;

    private String description;

    private int joinCount;

    private Boolean isEnd;

    private Boolean isDateFixed;

    private List<JobInterest> jobInterests;

    public static MinimalGoalDetailDTO of(Goal goal) {
        return MinimalGoalDetailDTO.builder()
                    .id(goal.getId())
                    .title(goal.getTitle())
                    .description(goal.getDescription())
                    .joinCount(goal.getJoinCount())
                    .isEnd(goal.getIsEnd())
                    .isDateFixed(goal.getIsDateFixed())
                    .jobInterests(goal.getGoalJobInterests()
                            .stream()
                            .map(GoalJobInterest::getJobInterest)
                            .collect(Collectors.toList()))
                .build();
    }
}
