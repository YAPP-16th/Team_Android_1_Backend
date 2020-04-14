package com.eroom.erooja.features.goal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GoalSearchRequestDTO {

    private GoalFilterBy goalFilterBy;

    private String keyword;

    private LocalDateTime startDt;

    private LocalDateTime endDt;

    private Set<Long> jobInterestIds;

    private GoalSortBy goalSortBy = GoalSortBy.ID;

    private Sort.Direction direction = Sort.Direction.ASC;

    private int size = 10;

    private int page;

    public void setStartDt(String startDt) {
        this.startDt = LocalDateTime.parse(startDt);
    }

    public void setEndDt(String endDt) {
        this.endDt = LocalDateTime.parse(endDt);
    }
}
