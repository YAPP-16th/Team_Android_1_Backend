package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.domain.enums.GoalRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // 0.0 ~ 1.0, (체크한 투두 수) / (전체 할 일 수) 비율
    private Double checkedTodoRate;

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
