package com.eroom.erooja.features.todo.service;

import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.todo.dto.TodoDTO;
import com.eroom.erooja.features.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;i
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(TodoService.class);

    public List<Todo> addTodo(String uid, Long goalId, List<TodoDTO> todoDTOList) {
        List<Todo> todoList = todoDTOList.stream()
                .map(todoDTO -> {
                    Todo todo = modelMapper.map(todoDTO, Todo.class);
                    todo.setIsEnd(false);
                    todo.setMemberGoal(MemberGoal.builder().uid(uid).goalId(goalId).build());
                    return todo;
                })
                .collect(Collectors.toList());

        return todoRepository.saveAll(todoList);
    }
}
