package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.common.exception.GoalNotFoundException;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.repos.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
public class GoalDetailServiceTest {
    private final GoalService goalService;
    @MockBean
    private GoalRepository goalRepository;

    @Test
    @DisplayName("목표 상세조회 (성공)")
    public void findGoalById_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        Long goalId = 1L;

        Goal newGoal = Goal.builder()
                .id(goalId)
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false).build();

        given(goalRepository.findById(goalId)).willReturn(Optional.of(newGoal));

        //when
        Goal findGoal = goalService.findGoalById(goalId);

        //then
        assertAll(
                ()->assertThat(findGoal.getId()).isNotNull(),
                ()->assertThat(findGoal.getStartDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(findGoal.getEndDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(findGoal.getTitle()).isEqualTo("title"),
                ()->assertThat(findGoal.getDescription()).isEqualTo("description"),
                ()->assertThat(findGoal.getIsDateFixed()).isEqualTo(false),
                ()->assertThat(findGoal.getIsEnd()).isEqualTo(false)
        );
    }

    @Test
    @DisplayName("목표 상세조회 (실패 - 잘못된 id조회)")
    public void findGoalById_fail_if_wrongId() throws Exception {
        assertThrows(
                GoalNotFoundException.class,
                () -> goalService.findGoalById(9999L));
    }
}
