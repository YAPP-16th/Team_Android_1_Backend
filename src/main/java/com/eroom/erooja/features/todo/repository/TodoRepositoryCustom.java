package com.eroom.erooja.features.todo.repository;

import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
    Page<Todo> getTodoListByGoalIdAndUid(Pageable pageable, Long goalId, String uid);
}
