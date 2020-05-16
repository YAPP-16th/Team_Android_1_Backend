package com.eroom.erooja.features.todo.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTodoDTO {
    private Long todoId;
    @NotEmpty(message = "할일 내용을 전송해야합니다.")
    private String content;
    @NotNull(message = "할일 우선순위를 전송해야합니다.")
    private int priority;
    @NotNull(message="할일종료 여부를 전송해야합니다.")
    private Boolean isEnd;
}
