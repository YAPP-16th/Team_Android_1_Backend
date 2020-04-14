package com.eroom.erooja.features.goal.dto;

import com.eroom.erooja.domain.field.Goal_;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalSortBy {
    ID(Goal_.id),
    TITLE(Goal_.title),
    START_DT(Goal_.startDt),
    END_DT(Goal_.endDt),
    JOINT_CNT(Goal_.joinCount);

    private String field;
}
