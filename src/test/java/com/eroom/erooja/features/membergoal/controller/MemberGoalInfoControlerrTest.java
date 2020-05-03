package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Profile({"test"})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class MemberGoalInfoControlerrTest {
    @MockBean
    MemberGoalService memberGoalService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private final MockMvc mockMvc;

    @Test
    @DisplayName("특정목표 자신의 참여정보 조회 (성공)")
    public void joinGoal_get_info_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);
        String mockUid = "onwerUid";

        Goal goal = Goal.builder()
                .id(0L).build();

        MemberGoal memberGoal = MemberGoal.builder()
                .goalId(goal.getId())
                .uid(mockUid)
                .isEnd(false)
                .copyCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .createDt(startDt)
                .updateDt(startDt)
                .role(GoalRole.OWNER).build();

        given(jwtTokenProvider.getUidFromHeader("Bearer [TOKEN]"))
                .willReturn(mockUid);
        given(memberGoalService.getGoalJoinByUidAndGoalId(anyString(),anyLong()))
                .willReturn(memberGoal);

        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/v1/membergoal/{goalId}/info", goal.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer [TOKEN]"))
                .andDo(print())
                .andExpect(status().isOk());

        //Documentation
        resultActions.andDo(
                document("join-get-info",
                        requestHeaders(
                                headerWithName("Authorization").description("jwt 토큰 Bearer type")
                        ),
                        pathParameters(
                                parameterWithName("goalId").description("조회하고자 하는 대상 goalId")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("goalId").description("달성할리스트"),
                                fieldWithPath("uid").description("사용자 구분값"),
                                fieldWithPath("startDt").description("목표참여 시작일"),
                                fieldWithPath("endDt").description("목표참여 종료일"),
                                fieldWithPath("role").description("목표참여 역할 (OWNER/PARTICIPANT)"),
                                fieldWithPath("isEnd").description("목표참여 종료여부"),
                                fieldWithPath("copyCount").description("사용자들이 담아간 횟수"),
                                fieldWithPath("createDt").description("참여날짜"),
                                fieldWithPath("updateDt").description("참여수정날짜")
                        )
                ));
    }

}
