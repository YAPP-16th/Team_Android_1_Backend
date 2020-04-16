package com.eroom.erooja.features.membergoal.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewGoalJoinRequestDTO {
    Long goalId;
    @Future(message = "반드시 미래 날짜여야합니다.")
    @NotNull(message = "종료 날짜를 선택해야 합니다.")
    LocalDateTime endDt;
}
