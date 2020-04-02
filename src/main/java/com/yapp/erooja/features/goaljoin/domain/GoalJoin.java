package com.yapp.erooja.features.goaljoin.domain;

import com.yapp.erooja.features.goal.domain.Goal;
import com.yapp.erooja.features.members.domain.Members;
import com.yapp.erooja.features.members.domain.Role;
import com.yapp.erooja.features.todo.domain.Todo;
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
    private Role role;
    @NonNull
    private Boolean isEnd=false;
    @NonNull
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
