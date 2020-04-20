package com.eroom.erooja.features.todo.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoDTO {
    private String content;
    private int priority;
}
