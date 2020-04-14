package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.domain.enums.GoalRole;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", updatable = false, insertable = false)
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", updatable = false, insertable = false)
    private Members member;

    @OneToMany(mappedBy = "memberGoal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList;
}
