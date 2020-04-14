package com.eroom.erooja.features.membergoal.service;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.membergoal.repository.MemberGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberGoalService {
    private final MemberGoalRepository memberGoalRepository;
    private final GoalRepository goalRepository;

    public MemberGoal joinGoal(String uid, Long goalId, LocalDateTime startDt, LocalDateTime endDt, GoalRole goalRole){
        return memberGoalRepository.save(MemberGoal.builder()
                .goalId(goalId)
                .uid(uid)
                .copyCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .role(goalRole)
                .isEnd(false).build());
    }
}
