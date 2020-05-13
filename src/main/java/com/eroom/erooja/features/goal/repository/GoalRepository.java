package com.eroom.erooja.features.goal.repository;

import com.eroom.erooja.domain.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, JpaSpecificationExecutor<Goal> {
    @Query("SELECT g FROM Goal g JOIN GoalJobInterest i " +
            "ON g.id = i.goal.id " +
            "AND i.jobInterest.id = :interestId " +
            "AND g.id not in (SELECT m.goalId FROM MemberGoal m WHERE m.uid = :uid)")
    Page<Goal> findGoalByInterestId(Long interestId, String uid, Pageable pageable);
}
