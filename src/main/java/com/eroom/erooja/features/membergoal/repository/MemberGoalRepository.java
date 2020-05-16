package com.eroom.erooja.features.membergoal.repository;

import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.MemberGoalPK;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MemberGoalRepository extends JpaRepository<MemberGoal, MemberGoalPK>, MemberGoalRepositoryCustom {
    Page<MemberGoal> findAllByUid(String uid, Pageable pageable);

    Page<MemberGoal> findAllByGoal_Id(Long goalId, Pageable pageable);

    int countMemberGoalByGoalId(Long goalId);

    Page<GoalJoinTodoDto> getJoinTodoListByGoalId(Long goalId, Pageable pageable);

    Page<MemberGoal> findAllByGoalId(Long goalId, Pageable pageable);

    @Query(value = "SELECT mg FROM MemberGoal mg WHERE mg.uid = :uid and (mg.endDt > :now OR mg.isEnd = true)")
    Page<MemberGoal> findAllByUidAndEndDtIsAfterOrIsEndTrue(@Param("uid") String uid, @Param("now") LocalDateTime now, Pageable pageable);

    Page<MemberGoal> findAllByUidAndEndDtIsBeforeAndIsEndFalse(String uid, LocalDateTime now, Pageable pageable);

    List<MemberGoal> findAllByEndDtIsAfter(LocalDateTime targetTime);
}
