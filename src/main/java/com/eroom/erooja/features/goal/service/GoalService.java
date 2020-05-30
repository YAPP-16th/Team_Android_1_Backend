package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.domain.repos.MemberRepository;
import com.eroom.erooja.features.goal.dto.GoalListResponse;
import com.eroom.erooja.features.goal.dto.UpdateGoalRequestDTO;
import com.eroom.erooja.features.goal.exception.GoalNotFoundException;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.domain.specification.GoalCriteria;
import com.eroom.erooja.domain.specification.GoalSpecifications;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    public Goal createGoal(CreateGoalRequestDTO createGoalDTO) {
        return goalRepository.save(Goal.builder()
                .isDateFixed(createGoalDTO.getIsDateFixed())
                .title(createGoalDTO.getTitle())
                .description(createGoalDTO.getDescription())
                .startDt(LocalDateTime.now())
                .endDt(createGoalDTO.getEndDt())
                .isEnd(false)
                .joinCount(1).build());
    }

    public Goal findGoalById(Long goalId) throws GoalNotFoundException {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_NOT_FOUND));
    }

    public Page<GoalListResponse> findGoalListByInterestId(String uid, Long interestId, Pageable pageable) {
        Page<Goal> goalPage = goalRepository.findGoalByInterestId(interestId, uid, pageable);

        return goalPage.map((goal) -> {
            List<String> userImages = memberRepository.getUserImageListByGoalId(goal.getId(), PageRequest.of(0, 3));
            return new GoalListResponse(goal, userImages);
        });
    }

    public Page<GoalListResponse> search(GoalCriteria goalCriteria) {
        Page<Goal> goalPage;

        goalPage = goalRepository.findAll(new GoalSpecifications(goalCriteria), goalCriteria.getPageRequest());

        return goalPage.map((goal) -> {
            List<String> userImages = memberRepository.getUserImageListByGoalId(goal.getId(), PageRequest.of(0, 3));
            return new GoalListResponse(goal, userImages);
        });
    }

    public void increaseJoinCount(Long goalId) throws GoalNotFoundException {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_NOT_FOUND));
        goal.increaseJoinCount(1);
        goalRepository.save(goal);
    }

    public Goal updateGoal(Long goalId, UpdateGoalRequestDTO updateGoalRequest) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_NOT_FOUND));

        if (updateGoalRequest.isTitleChanged())
            goal.setTitle(updateGoalRequest.getTitle());

        if (updateGoalRequest.isDescriptionChanged())
            goal.setDescription(updateGoalRequest.getDescription());

        return goalRepository.save(goal);
    }

    public void updateFinishedGoalToEnd(){
        LocalDateTime updateDt = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);
        List<Goal> finishGoals = goalRepository.findFinishedGoals(updateDt);

        finishGoals.forEach((goal)->{
            goal.setIsEnd(true);
        });

        goalRepository.saveAll(finishGoals);
    }
}