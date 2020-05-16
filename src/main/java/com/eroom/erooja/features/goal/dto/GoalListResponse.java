package com.eroom.erooja.features.goal.dto;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.JobInterest;
import lombok.*;

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
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private List<JobInterest> jobInterests;
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
        this.createDt = goal.getCreateDt();
        this.updateDt = goal.getUpdateDt();
        this.jobInterests = goal.jobInterests();
        this.userImages = userImages;
    }
}
