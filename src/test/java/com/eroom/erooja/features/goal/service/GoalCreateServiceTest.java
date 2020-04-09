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
public class GoalCreateServiceTest {
    private final GoalService goalService;
    private final GoalRepository goalRepository;

    @BeforeEach
    public void setUp() {
        goalRepository.deleteAll();
    }

    @Test
    @DisplayName("목표 생성 (성공)")
    public void createGoal_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);

        CreateGoalRequestDTO createGoalRequest = CreateGoalRequestDTO.builder()
                .startDt(startDt)
                .endDt(endDt)
                .title("title")
                .description("description")
                .isDateFixed(false).build();

        //when
        Goal newGoal = goalService.createGoal(createGoalRequest);

        //then
        assertAll(
                ()->assertThat(newGoal.getId()).isNotNull(),
                ()->assertThat(newGoal.getStartDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(newGoal.getEndDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(newGoal.getCreateDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(newGoal.getUpdateDt()).isInstanceOfAny(LocalDateTime.class),
                ()->assertThat(newGoal.getTitle()).isEqualTo("title"),
                ()->assertThat(newGoal.getDescription()).isEqualTo("description"),
                ()->assertThat(newGoal.getIsDateFixed()).isEqualTo(false),
                ()->assertThat(newGoal.getIsEnd()).isEqualTo(false)
        );
    }
}
