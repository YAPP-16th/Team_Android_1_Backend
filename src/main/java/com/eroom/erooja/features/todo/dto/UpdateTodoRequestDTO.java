package com.eroom.erooja.features.todo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTodoRequestDTO {
    @NotNull(message="goalId를 전송해야합니다.")
    public Long goalId;
    public List<UpdateTodoDTO> todoList;
}
