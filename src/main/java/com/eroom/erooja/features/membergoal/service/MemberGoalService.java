package com.eroom.erooja.features.membergoal.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.*;
import com.eroom.erooja.features.goal.exception.GoalNotFoundException;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.member.service.MemberJobInterestService;
import com.eroom.erooja.features.membergoal.dto.*;
import com.eroom.erooja.features.membergoal.repository.MemberGoalRepository;
import com.eroom.erooja.features.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class MemberGoalService {
    private final MemberJobInterestService memberJobInterestService;
    private final MemberGoalRepository memberGoalRepository;
    private final GoalRepository goalRepository;
    private final GoalService goalService;
    private final TodoService todoService;
    private final EntityManager em;

    @Transactional
    public MemberGoal joinExistGoal(String uid, GoalRole goalRole, GoalJoinRequestDTO goalJoinRequest) {
        Goal goal = goalRepository.findById(goalJoinRequest.getGoalId())
                .orElseThrow(() -> new EroojaException(ErrorEnum.GOAL_NOT_FOUND));

        if (goal.isTerminated())
            throw new EroojaException(ErrorEnum.GOAL_TERMINATED);

        if (goalJoinRequest.isExistOwnerUid())
            increaseCopyCount(goalJoinRequest.getOwnerUid(), goalJoinRequest.getGoalId());

        if(!(isAlreadyExistJoin(uid, goal.getId())))
            goalService.increaseJoinCount(goal.getId());

        if(isAlreadyExistJoin(uid, goal.getId()))
            todoService.deleteTodoAll(goal.getId(), uid);

        MemberGoal memberGoal = null;
        if (goal.getIsDateFixed())
            memberGoal = addMemberGoal(uid, goal.getId(), goal.getEndDt(), goalRole);
        else
            memberGoal = addMemberGoal(uid, goal.getId(), goalJoinRequest.getEndDt(), goalRole);

        em.flush();
        todoService.addTodo(uid, goal.getId(), goalJoinRequest.getTodoList());

        return memberGoal;
    }

    public void increaseCopyCount(String uid, Long goalId) {
        MemberGoal memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_NOT_FOUND));

        memberGoal.increaseCopyCount();
        memberGoalRepository.save(memberGoal);
    }

    public Boolean isAlreadyExistJoin(String uid, Long goalId) {
        Optional<MemberGoal> memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId));

        if (memberGoal.isPresent())
            return true;

        return false;
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

    @Transactional
    public Page<GoalJoinMemberDTO> getEndedGoalJoinPageByUid(String uid, Pageable pageable) {
        Page<MemberGoal> memberGoals = memberGoalRepository.findAllByUidAndEndDtIsBeforeOrIsEndTrue(uid, LocalDateTime.now(), pageable);
        return convertPage2DTO(memberGoals);
    }

    @Transactional
    public Page<GoalJoinMemberDTO> getGoalJoinPageByUid(String uid, Pageable pageable) {
        Page<MemberGoal> memberGoals = memberGoalRepository.findAllByUidAndEndDtIsAfterAndIsEndFalse(uid, LocalDateTime.now(), pageable);
        return convertPage2DTO(memberGoals);
    }

    private Page<GoalJoinMemberDTO> convertPage2DTO(Page<MemberGoal> origin) {
        return new PageImpl<>(
                origin.getContent().stream()
                        .map(mg -> GoalJoinMemberDTO.of(mg, goalService.findGoalById(mg.getGoalId())))
                        .collect(Collectors.toList()),
                origin.getPageable(), origin.getTotalElements());
    }

    public MemberPageDTO getMembersAllByGoalId(Long goalId, Pageable pageable) {
        Page<MemberGoal> memberGoalPage = memberGoalRepository.findAllByGoal_Id(goalId, pageable);
        List<Members> members = memberGoalPage.getContent().stream()
                .map(MemberGoal::getMember)
                .collect(Collectors.toList());

        List<String> uidList = members.stream().map(Members::getUid).collect(Collectors.toList());
        Map<String, List<JobInterest>> jobInterestByUid = memberJobInterestService.getJobInterestsByUids(uidList);
        MemberPageDTO pageDTO = MemberPageDTO.of(memberGoalPage, jobInterestByUid);
        return pageDTO;
    }

    public int countGoalJoinByGoalId(Long goalId) {
        return memberGoalRepository.countMemberGoalByGoalId(goalId);
    }

    public Page<GoalJoinTodoDto> getJoinTodoListByGoalId(Long goalId, Pageable pageable) {
        return memberGoalRepository.getJoinTodoListByGoalId(goalId, pageable);
    }

    public MemberGoal againJoin(UpdateJoinRequestDTO updateGoalJoinRequest, String uid, Long goalId) {
        MemberGoal memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(() -> new EroojaException(ErrorEnum.GOAL_JOIN_NOT_FOUND));
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new EroojaException(ErrorEnum.GOAL_NOT_FOUND));

        memberGoal.setIsEnd(false);
        memberGoal.setStartDt(LocalDateTime.now());

        if (goal.getIsDateFixed()) {
            if(goal.isTimeBeforeNow())
                throw new EroojaException(ErrorEnum.GOAL_TERMINATED);
            memberGoal.setEndDt(goal.getEndDt());
        }else{
            memberGoal.setEndDt(updateGoalJoinRequest.getEndDt());
        }

        return memberGoalRepository.save(memberGoal);
    }

    public MemberGoal changeJoinToEnd(String uid, Long goalId) {
        MemberGoal memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(() -> new EroojaException(ErrorEnum.GOAL_JOIN_NOT_FOUND));

        memberGoal.setIsEnd(true);

        return memberGoalRepository.save(memberGoal);
    }

    public MemberGoal getGoalJoinByUidAndGoalId(String uid, Long goalId) {
        return memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_JOIN_NOT_FOUND));
    }

    public GoalRole getGoalRole(String uid, Long goalId) {
        MemberGoal memberGoal = memberGoalRepository.findById(new MemberGoalPK(uid, goalId))
                .orElseThrow(() -> new GoalNotFoundException(ErrorEnum.GOAL_JOIN_NOT_FOUND));

        return memberGoal.getRole();
    }

    public List<MemberGoal> getAllEndedYesterday() {
        LocalDateTime fromDt = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime toDt = LocalDateTime.of(fromDt.toLocalDate(), LocalTime.MAX);
        return memberGoalRepository.findAllByEndDtBetweenAndIsEndFalse(fromDt,toDt);
    }
}
