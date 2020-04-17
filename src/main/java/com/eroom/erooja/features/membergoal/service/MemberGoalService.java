package com.eroom.erooja.features.membergoal.service;

import com.eroom.erooja.common.exception.MemberGoalNotFoundException;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.MemberGoalPK;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.membergoal.dto.ExistGoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.dto.NewGoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.repository.MemberGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberGoalService {
    private final MemberGoalRepository memberGoalRepository;
    private final GoalRepository goalRepository;
    private final GoalService goalService;

    public MemberGoal joinExistGoal(String uid, ExistGoalJoinRequestDTO goalJoinRequest) {
        Goal goal = goalRepository.findById(goalJoinRequest.getGoalId())
                .orElseThrow(MemberGoalNotFoundException::new);

        increaseCopyCount(goalJoinRequest.getOwnerUid(), goalJoinRequest.getGoalId());
        goalService.increaseJoinCount(goal.getId());

        if(goal.getIsDateFixed())
            return joinGoal(uid, goalJoinRequest.getGoalId(), goal.getEndDt(), GoalRole.PARTICIPANT);
        else
            return joinGoal(uid, goalJoinRequest.getGoalId(), goalJoinRequest.getEndDt(), GoalRole.PARTICIPANT);
    }

    public MemberGoal joinNewGoal(String uid, NewGoalJoinRequestDTO newGoalJoinRequest){
        Goal goal = goalRepository.findById(newGoalJoinRequest.getGoalId())
                .orElseThrow(MemberGoalNotFoundException::new);

        goalService.increaseJoinCount(goal.getId());

        if(goal.getIsDateFixed())
            return joinGoal(uid, goal.getId(), goal.getEndDt(), GoalRole.PARTICIPANT);
        else
            return joinGoal(uid, goal.getId(), newGoalJoinRequest.getEndDt(), GoalRole.PARTICIPANT);
    }

    public void increaseCopyCount(String uid, Long goalId) {
        MemberGoal memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(MemberGoalNotFoundException::new);
        memberGoal.increaseCopyCount();
        memberGoalRepository.save(memberGoal);
    }

    public MemberGoal joinGoal(String uid, Long goalId, LocalDateTime endDt, GoalRole goalRole) {
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
