package com.eroom.erooja.features.todo.controller;

import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
@Controller
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/member/{uid}/goal/{goalId}")
    public ResponseEntity getTodoListByUidAndGoalId(@RequestParam Boolean isEnd, Pageable pageable, @PathVariable String uid, @PathVariable Long goalId) {
        return null;
    }
}

