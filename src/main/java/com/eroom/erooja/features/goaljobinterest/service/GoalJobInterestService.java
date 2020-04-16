package com.eroom.erooja.features.goaljobinterest.service;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.features.goaljobinterest.repository.GoalJobInterestRepository;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GoalJobInterestService {
    private final JobInterestRepository jobInterestRepository;
    private final GoalJobInterestRepository goalJobInterestRepository;

    public List<JobInterest> getJobGroupList(Long goalId) {
        return null;
    }

    public GoalJobInterest addJobInterestForGoal(Long goalId, Long jobInterestId) {
        return goalJobInterestRepository.save(
                GoalJobInterest.builder()
                .goal(Goal.builder().id(goalId).build())
                .jobInterest(JobInterest.builder().id(jobInterestId).build()).build());
    }

    public List<GoalJobInterest> addJobInterestListForGoal(Long goalId, List<Long> jobInterestId) {
        return jobInterestId.stream()
                .map(interestId-> addJobInterestForGoal(goalId, interestId))
                .collect(Collectors.toList());
    }
}
