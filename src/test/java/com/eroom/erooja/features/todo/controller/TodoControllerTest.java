package com.eroom.erooja.features.todo.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.todo.dto.TodoDTO;
import com.eroom.erooja.features.todo.service.TodoService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
public class TodoControllerTest {
    @MockBean
    private TodoService todoService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;


    @Test
    @DisplayName("특정회원 할일리스트 조회 (성공)")
    public void todo_search_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);
        String mockUid = "KAKAO@testId";

        List<TodoDTO> todoDTOList = new ArrayList();
        todoDTOList.add(TodoDTO.builder()
                .content("fisrt")
                .priority(0).build());
        todoDTOList.add(TodoDTO.builder()
                .content("two")
                .priority(1).build());
        todoDTOList.add(TodoDTO.builder()
                .content("three")
                .priority(2).build());

        //Page todoPage = new PageImpl(todoList);

        Goal goal = Goal.builder()
                .id(0L)
                .startDt(startDt)
                .endDt(endDt)
                .title("title")
                .joinCount(1)
                .description("description")
                .isDateFixed(false)
                .isEnd(false).build();

        MemberGoal memberGoal = MemberGoal.builder()
                .goalId(goal.getId())
                .uid(mockUid)
                .isEnd(false)
                .copyCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .role(GoalRole.OWNER).build();

        given(jwtTokenProvider.getUidFromHeader("Bearer [TOKEN]"))
                .willReturn(mockUid);
        given(todoService.getTodoListByGoalIdAndUid(any(PageRequest.class), anyLong(), anyString()))
                .willReturn(todoPage);


        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders
                .post("/api/v1//member/{uid}/goal/{goalId}",mockUid,goal.getId())
                .param("page", "0")
                .param("size", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer [TOKEN]"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("goalId").isNumber());

        resultActions.andDo(
                document("member-goal-join",
                        requestHeaders(headerWithName("Authorization").description("jwt 토큰 Bearer type")),
                        requestParameters(
                                parameterWithName("page").description("페이지 위치"),
                                parameterWithName("size").description("한 페이지당 조회할 크기")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].createDt").description("목표 생성일"),
                                fieldWithPath("content[].updateDt").description("목표 수정일"),
                                fieldWithPath("content[].id").description("목표 구분값"),
                                fieldWithPath("content[].content").description("관련직무 리스트"),
                                fieldWithPath("content[].isEnd").description("관련직무 리스트"),
                                fieldWithPath("content[].priority").description("관련직무 리스트")
                        )
                ));
    }
}
