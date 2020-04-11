package com.eroom.erooja.features.goal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateGoalRequestDTO {
    @Length(min=5, message = "목표명을 최소 5자 이상 입력해주세요.")
    @Length(max=50, message = "목표명은 50자 이내만 입력")
    private String title;

    private String description;

    @NotNull(message = "기간 고정을 선택해야 합니다.")
    private Boolean isDateFixed;

    @Future(message = "반드시 미래 날짜여야합니다.")
    @NotNull(message = "종료 날짜를 선택해야 합니다.")
    private LocalDateTime endDt;
}
