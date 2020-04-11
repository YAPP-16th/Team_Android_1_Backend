package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    @Query("select g from Goal g JOIN GoalJobInterest i " +
            "where g.id = i.goal.id " +
            "and i.jobInterest.id = :interestId")
    Page<Goal> findGoalByInterestId(Long interestId, Pageable pageable);
}
