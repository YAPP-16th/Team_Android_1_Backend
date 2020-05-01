package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.membergoal.dto.GoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import com.eroom.erooja.features.todo.dto.TodoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Profile({"test"})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class MemberGoalCountControllerTest {
    @MockBean
    MemberGoalService memberGoalService;
    private final MockMvc mockMvc;

    @Test
    @DisplayName("목표 참여자 수 조회 (성공)")
    public void joinGoal_count_success() throws Exception {
        //given
        Goal goal = Goal.builder()
                .id(0L)
                .build();

        given(memberGoalService.countGoalJoinByGoalId(goal.getId())).willReturn(2);

        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/v1/membergoal/{goalId}/count",goal.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());

        //Documentation
        resultActions.andDo(
                document("member-goal-count",
                        pathParameters(
                                parameterWithName("goalId").description("상세정보 대상 goalId")
                        )));

    }
}
