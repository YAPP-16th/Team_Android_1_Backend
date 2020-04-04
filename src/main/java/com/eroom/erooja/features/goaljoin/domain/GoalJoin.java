package com.eroom.erooja.features.goaljoin.domain;

import com.eroom.erooja.features.goal.domain.Goal;
import com.eroom.erooja.features.members.domain.Members;
import com.eroom.erooja.features.todo.domain.Todo;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class GoalJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GoalRole role;

    @Column(nullable = false)
    private Boolean isEnd=false;

    @Column(nullable = false)
    private int copyCount=0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    private LocalDateTime startDt;
    private LocalDateTime endDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members member;

    @OneToMany(mappedBy="goalJoin", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList;
}
