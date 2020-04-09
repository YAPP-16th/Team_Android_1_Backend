package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.repos.GoalRepository;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;

    public Goal createGoal(CreateGoalRequestDTO createGoalDTO){ ;
        return goalRepository.save(Goal.builder()
                .isDateFixed(createGoalDTO.getIsDateFixed())
                .title(createGoalDTO.getTitle())
                .description(createGoalDTO.getDescription())
                .startDt(createGoalDTO.getStartDt())
                .endDt(createGoalDTO.getEndDt())
                .isEnd(false)
                .joinCount(0).build());
    }

    public Goal findGoalById(Long goalId){
        return goalRepository.findById(goalId).get();
    }
}