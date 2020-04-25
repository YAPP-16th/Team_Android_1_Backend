package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.domain.enums.GoalRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(MemberGoalPK.class)
@Entity
public class MemberGoal extends AuditProperties {
    @Id
    private String uid;

    @Id
    @Column(name = "goal_id")
    private Long goalId;

    @Enumerated(EnumType.STRING)
    private GoalRole role;

    @Column(nullable = false)
    private Boolean isEnd = false;

    @Column(nullable = false)
    private int copyCount = 0;

    private LocalDateTime startDt;

    private LocalDateTime endDt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", updatable = false, insertable = false)
    private Goal goal;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", updatable = false, insertable = false)
    private Members member;

    @JsonIgnore
    @OneToMany(mappedBy = "memberGoal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList = new ArrayList<>();

    @JsonProperty(value = "todoList")
    public List<Todo> todoList() {
        if (todoList == null) {
            return Collections.emptyList();
        }
        return todoList;
    }

    @Builder
    public MemberGoal(LocalDateTime createDt, LocalDateTime updateDt, String uid,
                      Long goalId, GoalRole role, Boolean isEnd, int copyCount,
                      LocalDateTime startDt, LocalDateTime endDt) {
        super(createDt, updateDt);
        this.uid = uid;
        this.goalId=goalId;
        this.role=role;
        this.isEnd=isEnd;
        this.copyCount=copyCount;
        this.startDt=startDt;
        this.endDt=endDt;
    }

    public int increaseCopyCount(){
        return copyCount++;
    }
}
