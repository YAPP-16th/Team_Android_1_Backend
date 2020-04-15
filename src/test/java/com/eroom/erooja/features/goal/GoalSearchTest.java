package com.eroom.erooja.features.goal;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.goaljobinterest.repository.GoalJobInterestRepository;
import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.is;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
@Profile({"test"}) @ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoalSearchTest {
    private final String BASE_HOST = "/api/v1/goal";

    private final JobInterestService jobInterestService;
    private final GoalRepository goalRepository;
    private final GoalJobInterestRepository goalJobInterestRepository;

    private final MockMvc mockMvc;

    @BeforeAll
    public void setUpEntities() {
        long cnt = jobInterestService.setUpDefaultJobInterests();
        if (cnt < 3) return;

        LocalDateTime offset = LocalDateTime.now();

        Goal goal01_develop = goalRepository.save(
                Goal.builder()
                        .startDt(offset)
                        .endDt(offset.plusHours(2))
                        .title("에러 없애기")
                        .description("헛둘")
                        .isDateFixed(false)
                        .joinCount(0)
                        .isEnd(false)
                    .build());

        Goal goal02_develop = goalRepository.save(
                Goal.builder()
                        .startDt(offset.plusHours(1))
                        .endDt(offset.plusHours(3))
                        .title("에러 두번 없애기")
                        .description("셋넷")
                        .isDateFixed(false)
                        .joinCount(0)
                        .isEnd(false)
                    .build());

        List<GoalJobInterest> goalJobInterests_develop = new ArrayList<>();

        goalJobInterests_develop.add(buildNewGoalJobInterest(goal01_develop.getId(), 1L));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal01_develop.getId(), 3L));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal01_develop.getId(), 6L));
        goal01_develop.setGoalJobInterests(goalJobInterests_develop);

        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), 1L));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), 3L));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), 4L));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), 5L));
        goal02_develop.setGoalJobInterests(goalJobInterests_develop);

        goalRepository.save(goal01_develop);
        goalRepository.save(goal02_develop);

        Goal goal_design01 = goalRepository.save(
                    Goal.builder()
                        .startDt(offset.plusHours(-2))
                        .endDt(offset)
                        .title("에셋 만들기")
                        .description("다여")
                        .isDateFixed(false)
                        .joinCount(0)
                        .isEnd(false)
                .build());

        Goal goal_design02 = goalRepository.save(
                Goal.builder()
                        .startDt(offset.plusHours(-1))
                        .endDt(offset.plusHours(1))
                        .title("에셋 두번 만들기")
                        .description("칠팔")
                        .isDateFixed(false)
                        .joinCount(1)
                        .isEnd(false)
                    .build());

        List<GoalJobInterest> goalJobInterests_design = new ArrayList<>();

        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design01.getId(), 2L));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design01.getId(), 14L));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design01.getId(), 16L));
        goal_design01.setGoalJobInterests(goalJobInterests_design);

        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design02.getId(), 2L));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design02.getId(), 14L));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design02.getId(), 15L));
        goal_design02.setGoalJobInterests(goalJobInterests_design);

        goalRepository.save(goal_design01);
        goalRepository.save(goal_design02);
    }

    private GoalJobInterest buildNewGoalJobInterest(Long goalId, Long jobInterestId) {
        return goalJobInterestRepository.save(
                GoalJobInterest.builder()
                    .goal(Goal.builder().id(goalId).build())
                    .jobInterest(JobInterest.builder().id(jobInterestId).build())
                .build());
    }

    @Test
    @DisplayName("한글 키워드로 검색할 수 있다.")
    public void fullParameterSearch() throws Exception {
        this.mockMvc.perform(
                get(BASE_HOST)
                        .param("goalFilterBy", "TITLE")
                        .param("keyword", URLEncoder.encode("에러", "utf-8"))
                        .param("fromDt", LocalDateTime.now().minusHours(5).toString())
                        .param("toDt", LocalDateTime.now().plusHours(5).toString())
                        .param("jobInterestIds", "1,6,5,2,15")
                        .param("goalSortBy", "JOINT_CNT")
                        .param("direction", "DESC")
                        .param("size", "5")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("totalElements", is(2)));
    }

    @Test
    @DisplayName("페이지를 제외한 모든 파라미터는 NULL 일 수 있다.")
    public void nullableParamsSearch() throws Exception {
        this.mockMvc.perform(
                get(BASE_HOST)
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("totalElements", is(4)));
    }
}
