package com.eroom.erooja.features.todo.service;

import com.eroom.erooja.domain.model.Todo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Profile({"test"}) @ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
public class TodoServiceTest {
    private final TodoService todoService;

    @Test
    @DisplayName("우선순위 검증 (순차적으로 되었는지)(성공)")
    public void isCorrectPriority_success() throws Exception {
        //given
        List<Todo> todoList = new ArrayList();
        todoList.add(Todo.builder()
                .priority(0).build());
        todoList.add(Todo.builder()
                .priority(1).build());
        todoList.add(Todo.builder()
                .priority(2).build());
        todoList.add(Todo.builder()
                .priority(3).build());

        Boolean isNotCorrect = todoService.checkPriorityIsNotCorrect(todoList);

        //then
        assertThat(isNotCorrect).isEqualTo(false);
    }

    @Test
    @DisplayName("우선순위 검증 (순차적으로 되었는지)(실패)")
    public void isCorrectPriority_fail() throws Exception {
        //given
        List<Todo> todoList = new ArrayList();
        todoList.add(Todo.builder()
                .priority(0).build());
        todoList.add(Todo.builder()
                .priority(2).build());
        todoList.add(Todo.builder()
                .priority(3).build());

        Boolean isNotCorrect = todoService.checkPriorityIsNotCorrect((todoList));

        //then
        assertThat(isNotCorrect).isEqualTo(true);
    }
}
