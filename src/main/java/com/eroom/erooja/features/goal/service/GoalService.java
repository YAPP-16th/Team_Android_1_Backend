package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.features.goal.exception.GoalNotFoundException;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;

    public Goal createGoal(CreateGoalRequestDTO createGoalDTO){ ;
        return goalRepository.save(Goal.builder()
                .isDateFixed(createGoalDTO.getIsDateFixed())
                .title(createGoalDTO.getTitle())
                .description(createGoalDTO.getDescription())
                .startDt(LocalDateTime.now())
                .endDt(createGoalDTO.getEndDt())
                .isEnd(false)
                .joinCount(1).build());
    }

    public Goal findGoalById(Long goalId) throws GoalNotFoundException{
        return goalRepository
                .findById(goalId)
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_NOT_FOUND));
    }

    public Page<Goal> findGoalListByInterestId(Long interestId, Pageable pageable){
        return goalRepository
                .findGoalByInterestId(interestId, pageable);
    }

}