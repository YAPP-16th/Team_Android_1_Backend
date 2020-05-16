package com.eroom.erooja.features.todo.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddTodoDTO {
    private String content;
    private int priority;
}
