package com.eroom.erooja.features.goal.dto;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalListResponse {
    private Long id;
    private String title;
    private String description;
    private int joinCount;
    private Boolean isEnd;
    private Boolean isDateFixed;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private List<JobInterest> goalJobInterests;
    private List<String> userImages;

    public GoalListResponse(Goal goal, List<String> userImages){
        this.id = goal.getId();
        this.title = goal.getTitle();
        this.description = goal.getDescription();
        this.joinCount = goal.getJoinCount();
        this.isEnd = goal.getIsEnd();
        this.isDateFixed = goal.getIsDateFixed();
        this.startDt = goal.getStartDt();
        this.endDt = goal.getEndDt();
        this.goalJobInterests = goal.jobInterests();
        this.userImages = userImages;
    }
}
