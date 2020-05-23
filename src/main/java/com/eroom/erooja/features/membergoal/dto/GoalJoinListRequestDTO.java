package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.features.goal.dto.GoalSortBy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GoalJoinListRequestDTO {
    private String uid;
    private int size;
    private int page;

    private boolean isEnd = false;
    private GoalSortBy sortBy;
    private Sort.Direction direction;

    public Pageable getPageable() {
        if (sortBy == null) {
            this.sortBy = GoalSortBy.ID;
        }

        if (direction == null) {
            this.direction = Sort.Direction.ASC;
        }

        String prefix;
        switch (sortBy) {
            default:
                prefix = "goal."; break;
            case START_DT: case END_DT:
                prefix = "";
        }
        return PageRequest.of(page, size, Sort.by(direction, prefix + sortBy.getField()));
    }
}
