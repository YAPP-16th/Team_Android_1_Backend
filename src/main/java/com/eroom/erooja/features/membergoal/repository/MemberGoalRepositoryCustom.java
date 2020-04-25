package com.eroom.erooja.features.membergoal.repository;

import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

public interface MemberGoalRepositoryCustom {
    public Page<GoalJoinTodoDto> getJoinTodoListByGoalId(Long goalID, Pageable pageable);
}
