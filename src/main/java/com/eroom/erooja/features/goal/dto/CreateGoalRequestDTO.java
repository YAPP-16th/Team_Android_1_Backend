package com.eroom.erooja.features.goal.dto;

import com.eroom.erooja.features.todo.dto.TodoDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateGoalRequestDTO {
    @NotNull(message = "목표명을 선택해야 합니다.")
    @Length(min=5, message = "목표명을 최소 5자 이상 입력해주세요.")
    @Length(max=50, message = "목표명은 50자 이내만 입력")
    private String title;

    private String description;

    @NotNull(message = "기간 고정을 선택해야 합니다.")
    private Boolean isDateFixed;

    @Future(message = "반드시 미래 날짜여야합니다.")
    @NotNull(message = "종료 날짜를 선택해야 합니다.")
    private LocalDateTime endDt;

    @NotNull(message = "관련직무를 1개 이상 선택해주세요")
    @Size(min = 1, message = "관련직무를 1개 이상 선택해주세요")
    private List<Long> interestIdList;

    @NotNull(message = "1개 이상의 리스트를 추가해주세요")
    @Size(min = 1, message = "1개 이상의 리스트를 추가해주세요")
    private List<TodoDTO> todoList;
}
