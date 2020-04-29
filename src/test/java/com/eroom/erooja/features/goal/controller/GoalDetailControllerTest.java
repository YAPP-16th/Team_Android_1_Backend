package com.eroom.erooja.features.goal.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.features.goal.exception.GoalNotFoundException;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.features.goal.service.GoalService;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class GoalDetailControllerTest {
    @MockBean
    private GoalService goalService;
    private final MockMvc mockMvc;

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
                .joinCount(1)
                .isEnd(false)
                .updateDt(startDt)
                .createDt(startDt).build();

        given(goalService.findGoalById(goalId)).willReturn(newGoal);

        ResultActions resultActions = this.mockMvc.perform((RestDocumentationRequestBuilders
                .get("/api/v1/goal/{goalId}",goalId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(goalId))
                .andExpect(jsonPath("startDt").exists())
                .andExpect(jsonPath("endDt").exists())
                .andExpect(jsonPath("updateDt").exists())
                .andExpect(jsonPath("createDt").exists())
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("description").value("description"))
                .andExpect(jsonPath("joinCount").value(1))
                .andExpect(jsonPath("isDateFixed").value(false))
                .andExpect(jsonPath("isEnd").value(false));

        //Documentation
        resultActions.andDo(
                document("goal-detail",
                        pathParameters(
                                parameterWithName("goalId").description("상세정보 대상 goalId")),
                        relaxedResponseFields(
                                fieldWithPath("createDt").description("목표 생성일"),
                                fieldWithPath("updateDt").description("목표 수정일"),
                                fieldWithPath("id").description("목표 구분값"),
                                fieldWithPath("title").description("목표명"),
                                fieldWithPath("description").description("목표 상세설명"),
                                fieldWithPath("joinCount").description("목표 참여자 수"),
                                fieldWithPath("isEnd").description("목표 종료여부"),
                                fieldWithPath("isDateFixed").description("기간 고정여부"),
                                fieldWithPath("startDt").description("목표 시작일"),
                                fieldWithPath("endDt").description("목표 종료일")
                        )
                ));
    }

    @Test
    @DisplayName("목표 상세조회 (실패 - 잘못된id 조회)")
    public void getGoalDetail_fail_if_wrongId() throws Exception {
        Long goalId = 9999L;
        given(goalService.findGoalById(goalId)).willThrow(GoalNotFoundException.class);

        this.mockMvc.perform(get("/api/v1/goal/" + goalId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
