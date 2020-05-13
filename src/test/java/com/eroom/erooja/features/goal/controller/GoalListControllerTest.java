package com.eroom.erooja.features.goal.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.GoalJobInterest;
import com.eroom.erooja.domain.model.JobInterest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
                .updateDt(startDt)
                .createDt(startDt)
                .title("title")
                .description("description")
                .isDateFixed(false)
                .joinCount(1)
                .isEnd(false).build();

        List<Goal> goalList = new ArrayList();
        goalList.add(findGoal1);

        Page<Goal> goalPage = new PageImpl(goalList);

        given(goalService.findGoalListByInterestId(anyString(), anyLong(), any(PageRequest.class)))
                .willReturn(goalPage);

        //when, then
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/v1/goal/interest/{interestId}", interestId)
                .param("page", "0")
                .param("size", "1")
                .param("sort", "createDt,desc")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());

        //Documentation
        resultActions.andDo(
                document("goal-list-interest",
                        pathParameters(
                                parameterWithName("interestId").description("상세정보 대상 goalId")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 위치"),
                                parameterWithName("size").description("한 페이지당 조회할 크기"),
                                parameterWithName("sort").description("정렬방식")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[]").description("목표 리스트"),
                                fieldWithPath("content[].createDt").description("목표 생성일"),
                                fieldWithPath("content[].updateDt").description("목표 수정일"),
                                fieldWithPath("content[].id").description("목표 구분값"),
                                fieldWithPath("content[].title").description("목표명"),
                                fieldWithPath("content[].description").description("목표 상세설명"),
                                fieldWithPath("content[].joinCount").description("목표 참여자 수"),
                                fieldWithPath("content[].isEnd").description("목표 종료여부"),
                                fieldWithPath("content[].isDateFixed").description("기간 고정여부"),
                                fieldWithPath("content[].startDt").description("목표 시작일"),
                                fieldWithPath("content[].endDt").description("목표 종료일"),
                                fieldWithPath("content[].jobInterests[]").description("관련직무 리스트")
                        )
                ));
    }
}
