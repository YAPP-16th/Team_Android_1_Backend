package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.domain.enums.GoalRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder.Default;

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

    @Min(0) @Max(1)
    @Default
    private Double checkedTodoRate = .0;

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

    @Builder
    public MemberGoal(LocalDateTime createDt, LocalDateTime updateDt, String uid,
                      Long goalId, GoalRole role, Boolean isEnd, int copyCount, Double checkedTodoRate,
                      LocalDateTime startDt, LocalDateTime endDt) {
        super(createDt, updateDt);
        this.uid = uid;
        this.goalId = goalId;
        this.role = role;
        this.isEnd = isEnd;
        this.copyCount = copyCount;
        this.checkedTodoRate = checkedTodoRate;
        this.startDt = startDt;
        this.endDt = endDt;
    }

    public int increaseCopyCount(){
        return copyCount++;
    }
}
