package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.enums.GoalRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJoinRequestDTO {
    @NotNull(message="종료 여부를 전송해야합니다.")
    private Boolean changedIsEnd;
    @NotNull(message="종료날짜를 전송해야합니다.")
    @FutureOrPresent(message="반드시 미래날짜를 전송해야합니다.")
    private LocalDateTime endDt;
}
