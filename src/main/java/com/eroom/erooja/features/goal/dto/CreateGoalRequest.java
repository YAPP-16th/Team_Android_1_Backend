package com.eroom.erooja.features.goal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateGoalRequest {
    @NotEmpty(message = "제목이 있어야 합니다.")
    private String title;

    @NotEmpty(message = "본문이 있어야 합니다.")
    private String description;

    @NotNull(message = "기간 고정을 선택해야 합니다.")
    private Boolean isDateFixed;

    @PastOrPresent(message = "현재 혹은 과거날짜를 선택해야합니다.")
    @NotNull(message = "시작 날짜를 선택해야 합니다.")
    private LocalDateTime startDt;

    @FutureOrPresent(message = "반드시 미래 날짜여야합니다.")
    @NotNull(message = "종료 날짜를 선택해야 합니다.")
    private LocalDateTime endDt;
}
