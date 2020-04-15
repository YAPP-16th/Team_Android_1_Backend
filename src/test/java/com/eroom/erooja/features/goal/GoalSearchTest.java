package com.eroom.erooja.features.goal;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.features.goal.repository.GoalRepository;
import com.eroom.erooja.features.goaljobinterest.repository.GoalJobInterestRepository;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.is;


@SpringBootTest
@Disabled @Transactional
@AutoConfigureMockMvc(addFilters = false)
@Profile({"test"}) @ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoalSearchTest {
    private final String BASE_HOST = "/api/v1/goal";

    private final JobInterestRepository jobInterestRepository;
    private final GoalRepository goalRepository;
    private final GoalJobInterestRepository goalJobInterestRepository;

    private final MockMvc mockMvc;

    @BeforeAll
    public void setUpEntities() {
        JobInterest jobGroup_develop = JobInterest.builder()
                    .id(1L)
                    .name("개발")
                    .jobGroup(null)
                    .jobInterestType(JobInterestType.JOB_GROUP)
                .build();

        JobInterest jobGroup_design = JobInterest.builder()
                    .id(2L)
                    .name("디자인")
                    .jobGroup(null)
                    .jobInterestType(JobInterestType.JOB_GROUP)
                .build();

        jobGroup_develop = jobInterestRepository.save(jobGroup_develop);
        jobGroup_design = jobInterestRepository.save(jobGroup_design);

        Map<Long, JobInterest> interests = new HashMap<>();

        for (Long id : new Long[] {3L, 4L, 5L, 6L, 14L, 15L, 16L}) {
            boolean isDesign = id == 2L || id > 14L;
            String name = isDesign ? "디자인_" : "개발_";
            interests.put(id, jobInterestRepository.save(
                            JobInterest.builder()
                                    .id(id)
                                    .name(name + id)
                                    .jobGroup(isDesign ? jobGroup_design : jobGroup_develop)
                                    .jobInterestType(JobInterestType.JOB_INTEREST)
                                .build()));
        }

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

        goalJobInterests_develop.add(buildNewGoalJobInterest(goal01_develop.getId(), jobGroup_develop));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal01_develop.getId(), interests.get(3L)));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal01_develop.getId(), interests.get(6L)));
        goal01_develop.setGoalJobInterests(goalJobInterests_develop);

        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), jobGroup_develop));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), interests.get(3L)));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), interests.get(4L)));
        goalJobInterests_develop.add(buildNewGoalJobInterest(goal02_develop.getId(), interests.get(5L)));
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

        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design01.getId(), jobGroup_design));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design01.getId(), interests.get(14L)));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design01.getId(), interests.get(16L)));
        goal_design01.setGoalJobInterests(goalJobInterests_design);

        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design02.getId(), jobGroup_design));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design02.getId(), interests.get(14L)));
        goalJobInterests_design.add(buildNewGoalJobInterest(goal_design02.getId(), interests.get(15L)));
        goal_design02.setGoalJobInterests(goalJobInterests_design);

        goalRepository.save(goal_design01);
        goalRepository.save(goal_design02);
    }

    private GoalJobInterest buildNewGoalJobInterest(Long goalId, JobInterest jobInterest) {
        return goalJobInterestRepository.save(
                GoalJobInterest.builder()
                    .goal(Goal.builder().id(goalId).build())
                    .jobInterest(jobInterest)
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
                        .param("jobInterestIds", "1,6,5,2,3,15")
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
