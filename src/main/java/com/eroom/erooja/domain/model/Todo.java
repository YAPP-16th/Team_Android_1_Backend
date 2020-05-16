package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.features.todo.dto.UpdateTodoDTO;
import com.eroom.erooja.features.todo.dto.UpdateTodoRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Todo extends AuditProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @Column(nullable = false)
    private Boolean isEnd = false;

    @Column(nullable = false)
    private int priority;

    @Builder
    public Todo(Long id, String content, Boolean isEnd, int priority, LocalDateTime createDt, LocalDateTime updateDt) {
        super(createDt, updateDt);
        this.id = id;
        this.content = content;
        this.isEnd = isEnd;
        this.priority = priority;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "uid"),
            @JoinColumn(name = "goal_id")
    })
    MemberGoal memberGoal;

    public Boolean validTodoIsOwn(String uid) {
        if (this.getUid().equals(uid))
            return true;
        return false;
    }

    @JsonIgnore
    public String getUid() {
        return this.memberGoal.getUid();
    }

    public static Todo of(UpdateTodoDTO updateTodoDTO){
        return Todo.builder()
                .priority(updateTodoDTO.getPriority())
                .isEnd(updateTodoDTO.getIsEnd())
                .content(updateTodoDTO.getContent()).build();
    }
}
