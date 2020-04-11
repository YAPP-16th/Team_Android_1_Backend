package com.eroom.erooja.features.goaljobinterest.repository;

import com.eroom.erooja.domain.model.GoalJobInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalJobInterestRepository extends JpaRepository<GoalJobInterest, Long> {
}
