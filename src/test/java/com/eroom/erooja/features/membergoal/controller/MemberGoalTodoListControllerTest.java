package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.membergoal.dto.GoalJoinTodoDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
public class MemberGoalTodoListControllerTest {
    @MockBean
    MemberGoalService memberGoalService;
    private final MockMvc mockMvc;

    @Test
    @DisplayName("목표 참여리스트(+할일리스트) 조회 (성공)")
    public void joinGoal_todo_list_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        LocalDateTime endDt = startDt.plusHours(2);

        Goal goal = Goal.builder()
                .id(0L).build();

        MemberGoal memberGoal = MemberGoal.builder()
                .goalId(goal.getId())
                .uid("onwerUid")
                .isEnd(false)
                .copyCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .role(GoalRole.OWNER).build();

        List<Todo> todoList = new ArrayList();
        todoList.add(Todo.builder()
                .id(0L)
                .isEnd(false)
                .content("fisrt")
                .priority(0)
                .createDt(startDt)
                .updateDt(startDt).build());
        todoList.add(Todo.builder()
                .id(1L)
                .content("two")
                .isEnd(false)
                .priority(1)
                .createDt(startDt)
                .updateDt(startDt).build());
        todoList.add(Todo.builder()
                .id(2L)
                .content("three")
                .isEnd(false)
                .priority(2)
                .createDt(startDt)
                .updateDt(startDt).build());

        GoalJoinTodoDto goalJoinTodoDto = new GoalJoinTodoDto(
                memberGoal,
                todoList,
                "hyeonsu");

        List<GoalJoinTodoDto> goalJoinTodoList = new ArrayList();
        goalJoinTodoList.add(goalJoinTodoDto);
        Page<GoalJoinTodoDto> goalJoinTodoPage = new PageImpl(goalJoinTodoList);

        given(memberGoalService.getJoinTodoListByGoalId(anyLong(), any(PageRequest.class)))
                .willReturn(goalJoinTodoPage);

        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/v1/membergoal/{goalId}/todo", goal.getId())
                .param("page", "0")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk());

        //Documentation
        resultActions.andDo(
                document("join-todo-list",
                        pathParameters(
                                parameterWithName("goalId").description("조회하고자 하는 대상 goalId")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 위치"),
                                parameterWithName("size").description("한 페이지당 조회할 크기")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[]").description("목표 참여자리스트"),
                                fieldWithPath("content[].goalId").description("목표참여 구분값"),
                                fieldWithPath("content[].uid").description("사용자 구분값"),
                                fieldWithPath("content[].startDt").description("목표참여 시작일"),
                                fieldWithPath("content[].endDt").description("목표참여 종료일"),
                                fieldWithPath("content[].isEnd").description("목표참여 종료여부"),
                                fieldWithPath("content[].copyCount").description("사용자들이 담아간 횟수"),
                                fieldWithPath("content[].isEnd").description("목표참여 종료여부"),
                                fieldWithPath("content[].todoList[]").description("달성할리스트")
                        )
                ));
    }
}
