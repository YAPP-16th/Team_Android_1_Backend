package com.eroom.erooja.features.goal.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
public class GoalListServiceTest {
    private final GoalService goalService;
    @MockBean
    private GoalRepository goalRepository;

    @Test
    @DisplayName("관심직무로 목표탐색 (성공)")
    public void findGoalListByInterestId_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        Long interestId = 1L;

        Goal findGoal1 = Goal.builder()
                .id(2L)
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false).build();

        Goal findGoal2= Goal.builder()
                .id(3L)
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false).build();

        List<Goal> goalList = new ArrayList();
        goalList.add(findGoal1);
        goalList.add(findGoal2);

        Page<Goal> goalPage = new PageImpl(goalList);

        given(goalRepository.findGoalByInterestId(interestId,PageRequest.of(0,2))).willReturn(goalPage);

        //when
        Page<Goal> findGoalPage = goalService.findGoalListByInterestId(interestId, PageRequest.of(0,2));

        //then
        assertAll(
                ()->assertThat(findGoalPage.getSize()).isEqualTo(2),
                ()->assertThat(findGoalPage.getTotalElements()).isEqualTo(2)
        );
    }
}
