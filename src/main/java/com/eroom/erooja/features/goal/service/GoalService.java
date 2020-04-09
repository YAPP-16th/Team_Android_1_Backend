package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.features.goal.dto.CreateGoalRequest;
import org.springframework.stereotype.Service;

@Service
public class GoalService {
    public Goal createGoal(CreateGoalRequest createGoalDTO){
        return new Goal();
    }
}