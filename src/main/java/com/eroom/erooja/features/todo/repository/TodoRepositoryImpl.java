package com.eroom.erooja.features.todo.repository;

import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.QMembers;
import com.eroom.erooja.domain.model.QTodo;
import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        QueryResults<Todo> results = queryFactory
                .selectFrom(todo)
                .where(todo.memberGoal.goalId.eq(goalId),
                        todo.memberGoal.uid.eq(uid))
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
