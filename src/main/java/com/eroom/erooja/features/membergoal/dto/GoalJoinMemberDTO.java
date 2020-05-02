package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
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

    private int checkedTodoRate;

    private LocalDateTime startDt;

    private LocalDateTime endDt;

    MinimalGoalDetailDTO minimalGoalDetail;

    public static GoalJoinMemberDTO of(MemberGoal memberGoal, Goal goal) {
        return GoalJoinMemberDTO.builder()
                    .goalId(memberGoal.getGoalId())
                    .role(memberGoal.getRole())
                    .isEnd(memberGoal.getIsEnd())
                    .copyCount(memberGoal.getCopyCount())
                    .checkedTodoRate((int) (memberGoal.getCheckedTodoRate() * 100))
                    .startDt(memberGoal.getStartDt())
                    .endDt(memberGoal.getEndDt())
                    .minimalGoalDetail(MinimalGoalDetailDTO.of(goal))
                .build();
    }
}
