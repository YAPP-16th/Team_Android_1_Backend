package com.eroom.erooja.features.goal.controller;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.repos.GoalJobInterestRepository;
import com.eroom.erooja.domain.repos.GoalRepository;
import com.eroom.erooja.domain.repos.JobInterestRepository;
import com.eroom.erooja.features.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class GoalListControllerTest {
    @MockBean
    private GoalService goalService;
    private final MockMvc mockMvc;

    @Test
    @DisplayName("관심직무로 목표탐색 (성공)")
    public void getGoalListByInterestId_success() throws Exception {
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

        given(goalService.findGoalListByInterestId(interestId, PageRequest.of(0,2)))
                .willReturn(goalPage);

        //when, then
        this.mockMvc.perform(get("/api/v1/goal/interest/"+interestId+"?page=0&size=2")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
