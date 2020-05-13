package com.eroom.erooja.features.goal.repository;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.*;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import com.eroom.erooja.domain.repos.MemberAuthRepository;
import com.eroom.erooja.domain.repos.MemberRepository;
import com.eroom.erooja.features.goaljobinterest.repository.GoalJobInterestRepository;
import com.eroom.erooja.features.membergoal.repository.MemberGoalRepository;
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

import java.lang.reflect.Member;
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
    private final MemberGoalRepository memberGoalRepository;
    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;

    @Test
    @DisplayName("관심직무로 목표탐색 (성공)")
    public void findGoalListByInterestId_success() throws Exception {
        //given & when
        String mockUid1 = "mockUid";

        Goal saveGoal1 = goalRepository.save(Goal.builder()
                .title("goal1")
                .isDateFixed(false)
                .isEnd(false).build());

        Goal saveGoal2 = goalRepository.save(Goal.builder()
                .title("goal2")
                .isDateFixed(false)
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

        MemberAuth memberAuth = memberAuthRepository.save(MemberAuth.builder()
                .uid(mockUid1).build());

        memberRepository.save(Members.builder()
                .uid(mockUid1)
                .memberAuth(memberAuth).build());

        goalJobInterestRepository.save(GoalJobInterest.builder()
                .goal(saveGoal1)
                .jobInterest(saveServerInterest).build());

        goalJobInterestRepository.save(GoalJobInterest.builder()
                .goal(saveGoal2)
                .jobInterest(saveServerInterest).build());

        memberGoalRepository.save(
                MemberGoal.builder()
                        .uid(mockUid1)
                        .goalId(saveGoal1.getId())
                        .isEnd(false).build());

        Page<Goal> findGoalPage = goalRepository.findGoalByInterestId(saveServerInterest.getId(), mockUid1, PageRequest.of(0, 2));

        //then
        assertAll(
                () -> assertThat(findGoalPage.getContent().size()).isEqualTo(1),
                () -> assertThat(findGoalPage.getContent().get(0).getId()).isEqualTo(saveGoal2.getId())
        );
    }
}
