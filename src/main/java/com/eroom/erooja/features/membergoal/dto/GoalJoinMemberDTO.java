package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalJoinMemberDTO {
    private Long goalId;

    private GoalRole role;

    private Boolean isEnd;

    private int copyCount;

    private double checkedTodoRate;

    private LocalDateTime startDt;

    private LocalDateTime endDt;

    MinimalGoalDetailDTO minimalGoalDetail;

    public static GoalJoinMemberDTO of(MemberGoal memberGoal, Goal goal) {
        List<Todo> todoList = memberGoal.getTodoList();
        double checkedTodoRate = todoList.stream().filter(Todo::getIsEnd).count() / (double) todoList.size();

        return GoalJoinMemberDTO.builder()
                    .goalId(memberGoal.getGoalId())
                    .role(memberGoal.getRole())
                    .isEnd(memberGoal.getIsEnd())
                    .copyCount(memberGoal.getCopyCount())
                    .startDt(memberGoal.getStartDt())
                    .endDt(memberGoal.getEndDt())
                    .checkedTodoRate(checkedTodoRate)
                    .minimalGoalDetail(MinimalGoalDetailDTO.of(goal))
                .build();
    }
}
