package com.eroom.erooja.features.membergoal.repository;

import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.MemberGoalPK;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MemberGoalRepository extends JpaRepository<MemberGoal, MemberGoalPK>, MemberGoalRepositoryCustom {
    Page<MemberGoal> findAllByUid(String uid, Pageable pageable);

    Page<MemberGoal> findAllByGoal_Id(Long goalId, Pageable pageable);

    int countMemberGoalByGoalId(Long goalId);

    Page<GoalJoinTodoDto> getJoinTodoListByGoalId(Long goalId, Pageable pageable);
    Page<MemberGoal> findAllByGoalId(Long goalId, Pageable pageable);

    Page<MemberGoal> findAllByUidAndEndDtIsAfter(String uid, Pageable pageable, LocalDateTime now);

    Page<MemberGoal> findAllByUidAndEndDtIsBefore(String uid, Pageable pageable, LocalDateTime now);
}
