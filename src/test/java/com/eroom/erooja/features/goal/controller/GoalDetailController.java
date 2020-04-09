package com.eroom.erooja.features.goal.controller;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.repos.GoalRepository;
import com.eroom.erooja.features.goal.service.GoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

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
public class GoalDetailController {
    @MockBean
    private GoalService goalService;
    private final GoalRepository goalRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        goalRepository.deleteAll();
    }

    @Test
    @DisplayName("목표 상세조회 (성공)")
    public void getGoalDetail_success() throws Exception {
        //when, then
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

        given(goalService.findGoalById(goalId)).willReturn(newGoal);

        this.mockMvc.perform(get("/api/v1/goal/"+goalId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(goalId))
                .andExpect(jsonPath("start_dt").exists())
                .andExpect(jsonPath("end_dt").exists())
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("description").value("description"))
                .andExpect(jsonPath("join_count").value(0))
                .andExpect(jsonPath("is_date_fixed").value(false))
                .andExpect(jsonPath("is_end").value(false));
    }
}
