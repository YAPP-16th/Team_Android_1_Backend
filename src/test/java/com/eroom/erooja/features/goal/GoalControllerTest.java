package com.eroom.erooja.features.goal;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.features.goal.dto.CreateGoalRequest;
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
public class GoalControllerTest {
    @MockBean
    private GoalService goalService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("목표 생성 (성공)")
    public void createGoal_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);

        CreateGoalRequest createGoalRequest = CreateGoalRequest.builder()
                .startDt(startDt)
                .endDt(endDt)
                .title("title")
                .description("description")
                .isDateFixed(false).build();

        Goal newGoal = Goal.builder()
                .id(0L)
                .startDt(startDt)
                .endDt(endDt)
                .title("title")
                .description("description")
                .isDateFixed(false)
                .isEnd(false).build();

        //when, then
        given(goalService.createGoal(any(CreateGoalRequest.class))).willReturn(newGoal);

        this.mockMvc.perform(post("/api/v1/goal")
                .content(objectMapper.writeValueAsString(createGoalRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(0L))
                .andExpect(jsonPath("start_dt").exists())
                .andExpect(jsonPath("end_dt").exists())
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("description").value("description"))
                .andExpect(jsonPath("is_date_fixed").value(false))
                .andExpect(jsonPath("is_end").value(false));
    }
}
