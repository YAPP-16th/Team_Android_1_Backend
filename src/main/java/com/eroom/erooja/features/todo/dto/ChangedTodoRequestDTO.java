package com.eroom.erooja.features.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangedTodoRequestDTO {
    @NotNull(message = "변경하고자하는 상태를 전송하지 않았습니다.")
    private Boolean changedIsEnd;
}
