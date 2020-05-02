package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalJoinTodoDto {
    private String uid;
    private Long goalId;
    private GoalRole role;
    private Boolean isEnd;
    private int copyCount;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private List<Todo> todoList;
    private String nickName;

    public GoalJoinTodoDto(MemberGoal memberGoal, List<Todo> todoList, String nickName){
        this.uid = memberGoal.getUid();
        this.goalId=memberGoal.getGoalId();
        this.copyCount=memberGoal.getCopyCount();
        this.endDt=memberGoal.getEndDt();
        this.isEnd=memberGoal.getIsEnd();
        this.role=memberGoal.getRole();
        this.startDt=memberGoal.getStartDt();
        this.todoList=todoList;
        this.nickName=nickName;
    }
}
