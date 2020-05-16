package com.eroom.erooja.features.todo.controller;

import com.eroom.erooja.documentation.v1.RestDocsConfiguration;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Todo;
import com.eroom.erooja.features.todo.service.TodoService;
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
public class TodoControllerTest {
    @MockBean
    private TodoService todoService;
    private final MockMvc mockMvc;


    @Test
    @DisplayName("특정회원 할일리스트 조회 (성공)")
    public void todo_search_success() throws Exception {
        //given
        LocalDateTime startDt = LocalDateTime.now();
        String mockUid = "KAKAO@testId";

        Goal goal = Goal.builder()
                .id(0L)
                .isEnd(false).build();

        MemberGoal memberGoal = MemberGoal.builder()
                .goalId(goal.getId())
                .uid(mockUid)
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

        Page<Todo> todoPage = new PageImpl(todoList);

        given(todoService.getTodoListByGoalIdAndUid(any(PageRequest.class), anyLong(), anyString()))
                .willReturn(todoPage);

        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/v1/todo/")
                .param("uid", mockUid)
                .param("goalId", String.valueOf(goal.getId()))
                .param("page", "0")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk());

        //Documentation
        resultActions.andDo(
                document("todo-search",
                        requestParameters(
                                parameterWithName("goalId").description("상세정보 대상 goalId"),
                                parameterWithName("uid").description("상세정보 대상 uId"),
                                parameterWithName("page").description("페이지 위치"),
                                parameterWithName("size").description("한 페이지당 조회할 크기")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[]").description("할일 리스트"),
                                fieldWithPath("content[].createDt").description("할일 생성일"),
                                fieldWithPath("content[].updateDt").description("할일 수정일"),
                                fieldWithPath("content[].id").description("할일 구분값"),
                                fieldWithPath("content[].content").description("할일 내용"),
                                fieldWithPath("content[].isEnd").description("할일 종료여부"),
                                fieldWithPath("content[].priority").description("할일 우선순위")
                        )
                ));
    }
}
