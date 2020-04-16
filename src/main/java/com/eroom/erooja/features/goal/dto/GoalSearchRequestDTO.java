package com.eroom.erooja.features.goal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GoalSearchRequestDTO {

    private GoalFilterBy goalFilterBy;

    private String keyword;

    private LocalDateTime fromDt;

    private LocalDateTime toDt;

    private Set<Long> jobInterestIds;

    private GoalSortBy goalSortBy = GoalSortBy.ID;

    private Sort.Direction direction = Sort.Direction.ASC;

    @Min(5)
    private int size = 10;

    @Min(0)
    private int page;

    public void setFromDt(String fromDt) {
        this.fromDt = LocalDateTime.parse(fromDt);
    }

    public void setToDt(String toDt) {
        this.toDt = LocalDateTime.parse(toDt);
    }
}
