package com.eroom.erooja.features.todo.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.membergoal.controller.MemberGoalContoller;
import com.eroom.erooja.features.todo.dto.AddTodoDTO;
import com.eroom.erooja.features.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.eroom.erooja.common.constants.ErrorEnum.TODO_PRIORITY_NOT_CORRECT;

@RequiredArgsConstructor
@Service
public class TodoService {
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public List<Todo> addTodo(String uid, Long goalId, List<AddTodoDTO> todoDTOList) {
        List<Todo> todoList = mapAddDTOtoTodo(uid, goalId, todoDTOList);

        if (checkPriorityIsNotCorrect(todoList))
            throw new EroojaException(TODO_PRIORITY_NOT_CORRECT);

        return todoRepository.saveAll(todoList);
    }

    public Boolean checkPriorityIsNotCorrect(List<Todo> todoList) {
        int checkCount = 0;
        for (Todo todo : todoList) {
            if (todo.getPriority() != checkCount++)
                return true;
        }
        return false;
    }

    public Page<Todo> getTodoListByGoalIdAndUid(Pageable pageable, Long goalId, String uid){
        return todoRepository.getTodoListByGoalIdAndUid(pageable, goalId, uid);
    }

    public List<Todo> mapAddDTOtoTodo(String uid, Long goalId, List<AddTodoDTO> todoDTOList) {
        return todoDTOList.stream()
                .map(todoDTO -> {
                    Todo todo = modelMapper.map(todoDTO, Todo.class);
                    todo.setMemberGoal(MemberGoal.builder().uid(uid).goalId(goalId).build());
                    return todo;
                }).collect(Collectors.toList());
    }

    @Transactional
    public Todo changeEndState(String uid, Long todoId, Boolean changedIsEnd) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EroojaException(ErrorEnum.TODO_NOT_FOUND));

        if(!(todo.validTodoIsOwn(uid))) {
            logger.error("할일이 자신의 것이 아닙니다 / 요청uid : {} / 주인uid : {}",uid,todo.getUid());
            throw new EroojaException(ErrorEnum.TODO_NOT_OWNER);
        }

        todo.setIsEnd(changedIsEnd);
        return todoRepository.save(todo);
    }
}
