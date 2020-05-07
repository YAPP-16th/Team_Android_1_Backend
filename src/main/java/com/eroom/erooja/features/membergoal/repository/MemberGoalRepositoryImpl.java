package com.eroom.erooja.features.membergoal.repository;

import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.QMembers;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        JPAQuery<MemberGoal> query = queryFactory
                .selectFrom(memberGoal)
                .join(memberGoal.member, QMembers.members)
                .where(memberGoal.goalId.eq(goalID))
                .fetchJoin().offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(memberGoal.getType(),
                    memberGoal.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<MemberGoal> memberGoalList = query.fetchResults();
        List<GoalJoinTodoDto> goalJoinTodoDtoList = memberGoalList.getResults().stream()
                .map(join -> {
                            Hibernate.initialize(join.getTodoList());
                            return new GoalJoinTodoDto(
                                    join,
                                    join.getTodoList(),
                                    join.getMember().getNickname());
                        }
                )
                .collect(Collectors.toList());

        return new PageImpl<>(goalJoinTodoDtoList, pageable, query.fetchResults().getTotal());
    }
}
