package com.eroom.erooja.features.todo.repository;

import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.QMembers;
import com.eroom.erooja.domain.model.QTodo;
import com.eroom.erooja.domain.model.Todo;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.eroom.erooja.domain.model.QMemberGoal.memberGoal;
import static com.eroom.erooja.domain.model.QTodo.todo;


public class TodoRepositoryImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Todo> getTodoListByGoalIdAndUid(Pageable pageable, Long goalId, String uid){
        JPAQuery<Todo> query = queryFactory
                .selectFrom(todo)
                .where(todo.memberGoal.goalId.eq(goalId),
                        todo.memberGoal.uid.eq(uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(todo.getType(),
                    todo.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<Todo> results = query.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
