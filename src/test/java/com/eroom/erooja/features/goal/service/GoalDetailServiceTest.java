package com.eroom.erooja.features.goal.service;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.repos.GoalRepository;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
public class GoalDetailServiceTest {
    private final GoalService goalService;
    private final GoalRepository goalRepository;

    @BeforeEach
    public void setUp() {
        goalRepository.deleteAll();
    }

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
                .joinCount(0)
                .isEnd(false).build();

        goalRepository.save(newGoal);

        //when
        Goal findGoal = goalService.findGoalById(goalId);

        //then
        assertAll(
                ()->assertThat(findGoal.getId()).isNotNull(),
                ()->assertThat(findGoal.getStartDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(findGoal.getEndDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(findGoal.getCreateDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(findGoal.getUpdateDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(findGoal.getTitle()).isEqualTo("title"),
                ()->assertThat(findGoal.getDescription()).isEqualTo("description"),
                ()->assertThat(findGoal.getIsDateFixed()).isEqualTo(false),
                ()->assertThat(findGoal.getIsEnd()).isEqualTo(false)
        );
    }
}
