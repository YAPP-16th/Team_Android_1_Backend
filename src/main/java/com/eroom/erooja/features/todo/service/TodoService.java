package com.eroom.erooja.features.todo.service;

import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.todo.dto.AddTodoDTO;
import com.eroom.erooja.features.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.eroom.erooja.common.constants.ErrorEnum.TODO_PRIORITY_NOT_CORRECT;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public List<Todo> addTodo(String uid, Long goalId, List<AddTodoDTO> todoDTODTOList) {
        List<Todo> todoList = mapDTOtoTodo(uid, goalId, todoDTODTOList);

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

    public List<Todo> mapDTOtoTodo(String uid, Long goalId, List<AddTodoDTO> todoDTODTOList) {
        return todoDTODTOList.stream()
                .map(todoDTO -> {
                    Todo todo = modelMapper.map(todoDTO, Todo.class);
                    todo.setMemberGoal(MemberGoal.builder().uid(uid).goalId(goalId).build());
                    return todo;
                }).collect(Collectors.toList());
    }

    public List<>
}
