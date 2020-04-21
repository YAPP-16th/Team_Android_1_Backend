package com.eroom.erooja.features.membergoal.service;

import com.eroom.erooja.common.exception.MemberGoalNotFoundException;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.MemberGoalPK;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.membergoal.dto.GoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.repository.MemberGoalRepository;
import com.eroom.erooja.features.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberGoalService {
    private final MemberGoalRepository memberGoalRepository;
    private final GoalRepository goalRepository;
    private final GoalService goalService;
    private final TodoService todoService;

    public MemberGoal joinExistGoal(String uid, GoalJoinRequestDTO goalJoinRequest) {
        Goal goal = goalRepository.findById(goalJoinRequest.getGoalId())
                .orElseThrow(MemberGoalNotFoundException::new);

        if(goalJoinRequest.isExistOwnerUid())
            increaseCopyCount(goalJoinRequest.getOwnerUid(), goalJoinRequest.getGoalId());

        goalService.increaseJoinCount(goal.getId());

        MemberGoal memberGoal = null;
        if (goal.getIsDateFixed())
            memberGoal = addMemberGoal(uid, goal.getId(), goal.getEndDt(), GoalRole.PARTICIPANT);
        else
            memberGoal = addMemberGoal(uid, goal.getId(), goalJoinRequest.getEndDt(), GoalRole.PARTICIPANT);

        todoService.addTodo(uid, goal.getId(), goalJoinRequest.getTodoList());
        return memberGoal;
    }

    public void increaseCopyCount(String uid, Long goalId) {
        MemberGoal memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(MemberGoalNotFoundException::new);
        memberGoal.increaseCopyCount();
        memberGoalRepository.save(memberGoal);
    }


    public MemberGoal addMemberGoal(String uid, Long goalId, LocalDateTime endDt, GoalRole goalRole) {
        return memberGoalRepository.save(MemberGoal.builder()
                .goalId(goalId)
                .uid(uid)
                .copyCount(0)
                .startDt(LocalDateTime.now())
                .endDt(endDt)
                .role(goalRole)
                .isEnd(false).build());
    }
}
