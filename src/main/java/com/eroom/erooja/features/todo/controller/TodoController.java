package com.eroom.erooja.features.todo.controller;

import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.todo.dto.ChangedTodoRequestDTO;
import com.eroom.erooja.features.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
@Controller
public class TodoController {
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    private final TodoService todoService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity getTodoListByUidAndGoalId(Pageable pageable,
                                                    @RequestParam(required = true) String uid,
                                                    @RequestParam(required = true) Long goalId) {
        Page<Todo> todoPage = todoService.getTodoListByGoalIdAndUid(pageable, goalId, uid);
        return new ResponseEntity(todoPage, HttpStatus.OK);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity updateTodo(@RequestBody @Valid ChangedTodoRequestDTO endTodoRequest,
                                     @PathVariable Long todoId,
                                     @RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                     Errors errors) {
        if (errors.hasErrors()) {
            logger.error("error : {}", errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        String uid = jwtTokenProvider.getUidFromHeader(header);
        Todo changedTodo = todoService.changeEndState(uid, todoId, endTodoRequest.getChangedIsEnd());
        return new ResponseEntity(changedTodo, HttpStatus.OK);
    }
}

