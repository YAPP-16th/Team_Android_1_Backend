package com.eroom.erooja.features.membergoal.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExistGoalJoinRequestDTO {
    Long goalId;
    String ownerUid;
    @Future(message = "반드시 미래 날짜여야합니다.")
    @NotNull(message = "종료 날짜를 선택해야 합니다.")
    LocalDateTime endDt;
}
