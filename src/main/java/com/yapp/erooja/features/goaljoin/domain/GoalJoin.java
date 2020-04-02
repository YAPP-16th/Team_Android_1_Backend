package com.yapp.erooja.features.goaljoin.domain;

import com.yapp.erooja.features.goal.domain.Goal;
import com.yapp.erooja.features.members.domain.Members;
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
    private String title;
    @Lob
    private String description;
    @NonNull
    private int copyCount=0;
    @NonNull
    private Boolean isEnd=false;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members member;
    @OneToMany(mappedBy="goalJoin", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList;
}
