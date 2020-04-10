package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.common.exception.GoalNotFoundException;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.repos.GoalRepository;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Goal findGoalById(Long goalId) throws GoalNotFoundException{
        return goalRepository
                .findById(goalId)
                .orElseThrow(() -> new GoalNotFoundException("해당 목표가 존재하지 않습니다."));
    }

    public Page<Goal> findGoalListByInterestId(Long interestId, Pageable pageable){
        return null;
    }
}