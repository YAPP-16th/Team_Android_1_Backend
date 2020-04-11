package com.eroom.erooja.features.goal.controller;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import com.eroom.erooja.features.goal.service.GoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class GoalCreateControllerTest {
    @MockBean
    private GoalService goalService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    @DisplayName("목표 생성 (성공)")
    public void createGoal_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);

        CreateGoalRequestDTO createGoalRequest = CreateGoalRequestDTO.builder()
                .endDt(endDt)
                .title("title")
                .description("description")
                .isDateFixed(false).build();

        Goal newGoal = Goal.builder()
                .id(0L)
                .startDt(startDt)
                .endDt(endDt)
                .title("title")
                .joinCount(1)
                .description("description")
                .isDateFixed(false)
                .isEnd(false)
                .updateDt(startDt)
                .createDt(startDt).build();

        //when, then
        given(goalService.createGoal(any(CreateGoalRequestDTO.class))).willReturn(newGoal);

        this.mockMvc.perform(post("/api/v1/goal")
                .content(objectMapper.writeValueAsString(createGoalRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(0L))
                .andExpect(jsonPath("startDt").exists())
                .andExpect(jsonPath("endDt").exists())
                .andExpect(jsonPath("updateDt").exists())
                .andExpect(jsonPath("createDt").exists())
                .andExpect(jsonPath("joinCount").isNumber())
                .andExpect(jsonPath("title").isString())
                .andExpect(jsonPath("description").isString())
                .andExpect(jsonPath("isDateFixed").value(false))
                .andExpect(jsonPath("isEnd").value(false));
    }

    @Test
    @DisplayName("목표 생성 (실패 - 제목 5글자 이하)")
    public void createGoal_fail_if_titleShort() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);

        CreateGoalRequestDTO createGoalRequest = CreateGoalRequestDTO.builder()
                .endDt(endDt)
                .title("123")
                .description("description")
                .isDateFixed(false).build();

        //when, then
        this.mockMvc.perform(post("/api/v1/goal")
                .content(objectMapper.writeValueAsString(createGoalRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("목표 생성 (실패 - endDate 과거선택)")
    public void createGoal_fail_if_wrong_endDate() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.minusHours(2); //wrong date

        CreateGoalRequestDTO createGoalRequest = CreateGoalRequestDTO.builder()
                .endDt(endDt)
                .title("title")
                .description("description")
                .isDateFixed(false).build();

        //when, then
        this.mockMvc.perform(post("/api/v1/goal")
                .content(objectMapper.writeValueAsString(createGoalRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
