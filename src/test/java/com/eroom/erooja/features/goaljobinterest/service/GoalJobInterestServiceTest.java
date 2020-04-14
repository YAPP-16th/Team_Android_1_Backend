package com.eroom.erooja.features.goaljobinterest.service;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.features.goaljobinterest.repository.GoalJobInterestRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
public class GoalJobInterestServiceTest {
    private final GoalJobInterestService goalJobInterestService;
    @MockBean
    private GoalJobInterestRepository goalJobInterestRepository;
    private Goal saveGoal1;
    private Goal saveGoal2;
    private JobInterest saveDevelopInterest;
    private JobInterest saveServerInterest;
    private GoalJobInterest goalJobInterest;
    private GoalJobInterest goalJobInterest2;

    @BeforeEach
    void setUp() {
        LocalDateTime startDt = LocalDateTime.now();

        saveGoal1 = Goal.builder()
                .id(0L)
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false)
                .createDt(startDt)
                .updateDt(startDt).build();

        saveGoal2 = Goal.builder()
                .id(1L)
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false)
                .createDt(startDt)
                .updateDt(startDt).build();

        saveDevelopInterest = JobInterest.builder()
                .id(0L)
                .name("개발")
                .jobInterestType(JobInterestType.JOB_GROUP)
                .build();

        saveServerInterest = JobInterest.builder()
                .id(1L)
                .name("서버")
                .jobInterestType(JobInterestType.JOB_INTEREST)
                .jobGroup(saveDevelopInterest).build();

        goalJobInterest = GoalJobInterest.builder()
                .id(0L)
                .goal(saveGoal1)
                .jobInterest(saveServerInterest).build();

        goalJobInterest2 = GoalJobInterest.builder()
                .id(1L)
                .goal(saveGoal2)
                .jobInterest(saveServerInterest).build();
    }

    @Test
    @DisplayName("목표 관심직무 등록 (성공)")
    public void addJobInterest_for_goal_success() throws Exception {
        given(goalJobInterestRepository
                .save(any(GoalJobInterest.class)))
                .willReturn(goalJobInterest);

        //when
        GoalJobInterest goalJobInterest = goalJobInterestService.addJobInterestForGoal(0L, 1L);

        //then
        assertAll(
                () -> assertThat(goalJobInterest.getId()).isNotNull(),
                () -> assertThat(goalJobInterest.getGoal().getId()).isEqualTo(0L),
                () -> assertThat(goalJobInterest.getJobInterest().getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("목표 관심직무리스트 등록 (성공)")
    public void addJobInterestList_for_goal_success() throws Exception {
        given(goalJobInterestRepository
                .save(any(GoalJobInterest.class)))
                .willReturn(goalJobInterest);

        List<Long> interestIdList = new ArrayList();
        interestIdList.add(goalJobInterest.getId());
        interestIdList.add(goalJobInterest2.getId());

        //when
        List<GoalJobInterest> goalJobInterest = goalJobInterestService.addJobInterestListForGoal(0L, interestIdList);

        //then
        assertAll(
                () -> assertThat(goalJobInterest.size()).isEqualTo(2),
                () -> assertThat(goalJobInterest.get(0).getId()).isNotNull()
        );
    }
}
