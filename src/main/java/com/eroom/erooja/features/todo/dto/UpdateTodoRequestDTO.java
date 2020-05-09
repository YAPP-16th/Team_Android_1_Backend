package com.eroom.erooja.features.todo.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTodoRequestDTO {
    public Long goalId;
    public List<UpdateTodoDTO> todoList;
}
