package com.eroom.erooja.features.todo.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTodoRequestDTO {
    public Long goalId;
    public List<AddTodoDTO> todoList;
}
