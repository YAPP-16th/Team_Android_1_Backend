package com.eroom.erooja.features.goal.repository;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc(addFilters = false)
@DataJpaTest
public class GoalRepositoryTest {
    private final GoalRepository goalRepository;
    private final GoalJobInterestRepository goalJobInterestRepository;
    private final JobInterestRepository jobInterestRepository;

    @Test
    @DisplayName("관심직무로 목표탐색 (성공)")
    public void findGoalListByInterestId_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();

        Goal saveGoal1 = goalRepository.save(Goal.builder()
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false).build());

        Goal saveGoal2 = goalRepository.save(Goal.builder()
                .startDt(startDt)
                .endDt(startDt.plusHours(2))
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false).build());

        JobInterest saveDevelopInterest = jobInterestRepository.save(
                JobInterest.builder()
                .name("개발")
                .jobInterestType(JobInterestType.JOB_GROUP)
                .build());

        JobInterest saveServerInterest = jobInterestRepository.save(
                JobInterest.builder()
                .name("서버")
                .jobInterestType(JobInterestType.JOB_INTEREST)
                .jobGroup(saveDevelopInterest).build());

        GoalJobInterest goalJobInterest = goalJobInterestRepository.save(GoalJobInterest.builder()
                .goal(saveGoal1)
                .jobInterest(saveServerInterest).build());

        GoalJobInterest goalJobInterest2 = goalJobInterestRepository.save(GoalJobInterest.builder()
                .goal(saveGoal2)
                .jobInterest(saveServerInterest).build());

        //when
        Page<Goal> findGoalPage = goalRepository.findGoalByInterestId(saveServerInterest.getId(), PageRequest.of(0,2));

        //then
        assertAll(
                ()->assertThat(findGoalPage.getSize()).isEqualTo(2),
                ()->assertThat(findGoalPage.getTotalElements()).isEqualTo(2)
        );
    }
}
