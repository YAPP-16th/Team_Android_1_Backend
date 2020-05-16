package com.eroom.erooja.features.goal.service;

import com.amazonaws.services.dynamodbv2.xspec.L;
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
import com.eroom.erooja.features.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<GoalListResponse> findGoalListByInterestId(String uid, Long interestId, Pageable pageable) {
        List<GoalListResponse> goalListResponse = new ArrayList<>();

        Page<Goal> goalPage = goalRepository.findGoalByInterestId(interestId, uid, pageable);

        goalPage.getContent().stream().forEach((goal) -> {
            List<String> userImages = memberRepository.getUserImageListByGoalId(goal.getId(), PageRequest.of(0, 3));
            goalListResponse.add(new GoalListResponse(goal, userImages));
        });

        return goalListResponse;
    }

    public Page<Goal> search(GoalCriteria goalCriteria) {
        if (goalCriteria.getField() == null) {
            return goalRepository.findAll(goalCriteria.getPageRequest());
        } else {
            return goalRepository.findAll(new GoalSpecifications(goalCriteria), goalCriteria.getPageRequest());
        }
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
}