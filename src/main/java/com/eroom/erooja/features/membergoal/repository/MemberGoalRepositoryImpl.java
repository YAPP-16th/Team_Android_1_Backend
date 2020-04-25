package com.eroom.erooja.features.membergoal.repository;

import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.QMembers;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.eroom.erooja.domain.model.QMemberGoal.memberGoal;


public class MemberGoalRepositoryImpl implements MemberGoalRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberGoalRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public Page<GoalJoinTodoDto> getJoinTodoListByGoalId(Long goalID, Pageable pageable) {
        QueryResults<MemberGoal> results = queryFactory
                .selectFrom(memberGoal)
                .join(memberGoal.member, QMembers.members).fetchJoin()
                .where(memberGoal.goalId.eq(goalID))
                .fetchResults();

        List<MemberGoal> memberGoalList = results.getResults();

        List<GoalJoinTodoDto> goalJoinTodoDtoList = memberGoalList.stream().map(join ->{
                Hibernate.initialize(join.getTodoList());
                return new GoalJoinTodoDto(
                        join.getUid(),
                        join.getGoalId(),
                        join.getRole(),
                        join.getIsEnd(),
                        join.getCopyCount(),
                        join.getStartDt(),
                        join.getEndDt(),
                        join.getTodoList(),
                        join.getMember().getNickname());}
        )
                .collect(Collectors.toList());

        return new PageImpl<>(goalJoinTodoDtoList, pageable, results.getTotal());
    }
}
