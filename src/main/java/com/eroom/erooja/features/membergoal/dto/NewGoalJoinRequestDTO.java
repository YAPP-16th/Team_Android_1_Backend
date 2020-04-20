package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.features.todo.dto.TodoDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NewGoalJoinRequestDTO {
    Long goalId;

    @Future(message = "반드시 미래 날짜여야합니다.")
    @NotNull(message = "종료 날짜를 선택해야 합니다.")
    LocalDateTime endDt;

    @NotNull(message = "1개 이상의 리스트를 추가해주세요")
    @Size(min = 1, message = "1개 이상의 리스트를 추가해주세요")
    private List<TodoDTO> todoList;
}
