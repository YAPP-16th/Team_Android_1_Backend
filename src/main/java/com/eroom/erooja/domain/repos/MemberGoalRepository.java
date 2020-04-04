package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.MemberGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberGoalRepository extends JpaRepository<MemberGoal, Long> {
}
