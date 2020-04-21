package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.membergoal.dto.ExistGoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import com.eroom.erooja.features.todo.dto.TodoDTO;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
public class MemberGoalJoinControllerTest {
    @MockBean
    MemberGoalService memberGoalService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;


    @Test
    @DisplayName("고정된 목표 담기 (성공)")
    public void joinGoal_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);
        String mockUid = "KAKAO@testId";

        List<TodoDTO> todoDTOList = new ArrayList();
        todoDTOList.add(TodoDTO.builder()
                .content("fisrt")
                .priority(0).build());

        Goal goal = Goal.builder()
                .id(0L)
                .startDt(startDt)
                .endDt(endDt)
                .title("title")
                .joinCount(1)
                .description("description")
                .isDateFixed(false)
                .isEnd(false).build();

        MemberGoal existMemberGoal = MemberGoal.builder()
                .goalId(goal.getId())
                .uid("onwerUid")
                .isEnd(false)
                .copyCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .role(GoalRole.OWNER).build();

        MemberGoal newMemberGoal = MemberGoal.builder()
                .goalId(goal.getId())
                .uid(mockUid)
                .isEnd(false)
                .copyCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .updateDt(LocalDateTime.now())
                .createDt(LocalDateTime.now())
                .role(GoalRole.PARTICIPANT).build();

        ExistGoalJoinRequestDTO goalJoinRequest = ExistGoalJoinRequestDTO.builder()
                .goalId(goal.getId())
                .ownerUid(existMemberGoal.getUid())
                .endDt(endDt)
                .todoList(todoDTOList).build();

        given(jwtTokenProvider.getUidFromHeader("Bearer [TOKEN]"))
                .willReturn(mockUid);

        given(memberGoalService.joinExistGoal(eq(mockUid),
                any(ExistGoalJoinRequestDTO.class)))
                .willReturn(newMemberGoal);

        this.mockMvc.perform(post("/api/v1/membergoal")
                .content(objectMapper.writeValueAsString(goalJoinRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer [TOKEN]")
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("goalId").isNumber())
                .andExpect(jsonPath("uid").isString())
                .andExpect(jsonPath("startDt").exists())
                .andExpect(jsonPath("endDt").exists())
                .andExpect(jsonPath("copyCount").value(0))
                .andExpect(jsonPath("isEnd").isBoolean());
    }
}
