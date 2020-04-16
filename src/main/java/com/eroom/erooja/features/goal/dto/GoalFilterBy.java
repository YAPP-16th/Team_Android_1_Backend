package com.eroom.erooja.features.goal.dto;

import com.eroom.erooja.domain.field.Goal_;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalFilterBy {
    NONE(null), TITLE(Goal_.title), DESCRIPTION(Goal_.description);

    private String field;
}