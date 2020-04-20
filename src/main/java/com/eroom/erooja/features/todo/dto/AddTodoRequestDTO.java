package com.eroom.erooja.features.todo.dto;

import com.eroom.erooja.domain.model.Todo;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTodoRequestDTO {
    public Long goalId;
    public List<TodoDTO> todoList;
}
